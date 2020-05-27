package com.geek.security.distributed.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServer_53000Application {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServer_53000Application.class, args);
    }
}
