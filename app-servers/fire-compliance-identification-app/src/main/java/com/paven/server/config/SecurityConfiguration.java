package com.paven.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import com.paven.component.security.config.AuthorizeRequestsCustomizer;

/**
 * Security 配置
 *
 * @author paven
 */
@Configuration(proxyBeanMethods = false, value = "securityConfiguration")
public class SecurityConfiguration {

    @Bean("authorizeRequestsCustomizer")
    public AuthorizeRequestsCustomizer authorizeRequestsCustomizer() {
        return new AuthorizeRequestsCustomizer() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
                registry.requestMatchers(buildAgentApi("/auth/login")).anonymous()
                        .requestMatchers(buildAgentApi("/**")).authenticated();
            }
        };
    }
}