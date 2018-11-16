package com.project.alarmeweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.project.alarmeweb"})
public class AlarmeWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlarmeWebApplication.class, args);
    }
}
