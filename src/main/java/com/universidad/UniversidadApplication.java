package com.universidad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class UniversidadApplication {
    public static void main(String[] args) {
        SpringApplication.run(UniversidadApplication.class, args);
    }
}