package com.paven.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author paven
 */
@SpringBootApplication(scanBasePackages = {"${app.info.base-package}.server", "${app.info.base-package}.module"})
public class FireComplianceIdentificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FireComplianceIdentificationApplication.class, args);
    }

}
