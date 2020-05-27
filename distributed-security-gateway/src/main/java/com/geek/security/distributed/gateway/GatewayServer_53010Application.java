package com.geek.security.distributed.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class GatewayServer_53010Application {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServer_53010Application.class, args);
    }
}
