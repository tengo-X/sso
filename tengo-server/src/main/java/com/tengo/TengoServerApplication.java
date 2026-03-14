package com.tengo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication(scanBasePackages = "com.tengo")
public class TengoServerApplication {

    public static void main(String[] args) {
        System.out.println("TENGO.SSO: "+ UUID.randomUUID().toString().replaceAll("-",""));
        SpringApplication.run(TengoServerApplication.class, args);
    }

}
