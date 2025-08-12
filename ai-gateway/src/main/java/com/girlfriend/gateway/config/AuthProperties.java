package com.girlfriend.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "aigirlfriend.auth")
public class AuthProperties {
    private List<String> includePaths;
    private List<String> excludePaths;
}
