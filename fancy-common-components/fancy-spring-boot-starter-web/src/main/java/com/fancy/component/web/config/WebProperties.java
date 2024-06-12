package com.fancy.component.web.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @author paven
 */
@ConfigurationProperties(prefix = "app.web")
@Validated
@Data
public class WebProperties {

    @NotNull(message = "Agent API 不能为空")
    private Api agentApi = new Api("/agent-api", "**.controller.agent.**");
    @NotNull(message = "Admin API 不能为空")
    private Api adminApi = new Api("/admin-api", "**.controller.admin.**");

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Valid
    public static class Api {

        /**
         * API 前缀，实现所有 Controller 提供的 RESTFul API 的统一前缀
         */
        @NotEmpty(message = "API 前缀不能为空")
        private String prefix;

        /**
         * Controller 所在包的 Ant 路径规则
         */
        @NotEmpty(message = "Controller 所在包不能为空")
        private String controller;

    }

    @Data
    @Valid
    public static class Ui {

        /**
         * 访问地址
         */
        private String url;
    }
}
