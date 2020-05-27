package com.geek.security.ditributed.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Order_53021_Application {
    public static void main(String[] args) {
        SpringApplication.run(Order_53021_Application.class, args);
    }
}
