package com.natwest.trafficlight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TrafficLightApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrafficLightApplication.class, args);
    }

}
