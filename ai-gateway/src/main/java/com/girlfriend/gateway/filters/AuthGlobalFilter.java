package com.girlfriend.gateway.filters;

import com.aigirlfriend.commen.exception.UnauthorizedException;
import com.girlfriend.gateway.config.AuthProperties;
import com.girlfriend.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter {
    private final AuthProperties authProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final JwtTool jwtTool;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        RequestPath path = request.getPath();
        //判断放行
        if(isExclude(path.toString())){
            //放行
            return chain.filter(exchange);
        }
        //获取token
        String token = null;
        List<String> authorization = request.getHeaders().get("authorization");
        if(authorization != null && !authorization.isEmpty()){
            token = authorization.get(0);
        }
        Long userId = null;
        try{
            userId = jwtTool.parseToken(token);
        }catch (UnauthorizedException unauthorizedException){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //将用户id传入微服务
        String userinfo  = userId.toString();
        ServerWebExchange swe = exchange
                .mutate()
                .request(builder -> builder.header("userinfo",userinfo))
                .build();
        System.err.println("userId" + userinfo);
        return chain.filter(swe);
    }


    public boolean isExclude(String path){
        List<String> excludePaths = authProperties.getExcludePaths();
        for (String excludePath : excludePaths) {
            if(antPathMatcher.match(path,excludePath)){
                return true;
            }
        }
        return false;
    }
}
