package com.bykenyodarz.configserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigserveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigserveApplication.class, args);
    }

}
