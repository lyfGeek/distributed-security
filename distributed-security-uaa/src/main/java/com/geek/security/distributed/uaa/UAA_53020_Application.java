package com.geek.security.distributed.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableHystrix
@EnableFeignClients(basePackages = "com.geek.security.distributed.uaa")
@SpringBootApplication
public class UAA_53020_Application {
    public static void main(String[] args) {
        SpringApplication.run(UAA_53020_Application.class, args);
    }
}
