package com.fancy.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author paven
 */
@SpringBootApplication(scanBasePackages = {"${app.info.base-package}.server", "${app.info.base-package}.module"})
@EnableFeignClients(basePackages = "com.fancy.module.common.api")
public class AgentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentManagementApplication.class, args);
    }

}
