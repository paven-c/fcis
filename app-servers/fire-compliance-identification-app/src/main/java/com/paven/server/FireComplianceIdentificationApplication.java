package com.paven.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author paven
 */
@SpringBootApplication(scanBasePackages = {"${app.info.base-package}.server", "${app.info.base-package}.module"})
@EnableFeignClients(basePackages = "com.paven.module.common.api")
public class FireComplianceIdentificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FireComplianceIdentificationApplication.class, args);
    }

}
