package com.fancy.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author paven
 */
@SpringBootApplication(scanBasePackages = {"${app.info.base-package}.server", "${app.info.base-package}.module"})
public class AgentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentManagementApplication.class, args);
    }

}
