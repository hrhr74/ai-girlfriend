package com.aigirlfriend.commen.inceptor;

import cn.hutool.core.util.StrUtil;
import com.aigirlfriend.commen.utils.UserContext;
import com.ctc.wstx.shaded.msv_core.util.StringRef;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("user-info");
        if(StrUtil.isNotBlank(header)){
            UserContext.setUser(Long.valueOf(header));
            System.out.println("userId: " + Long.valueOf(header));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
