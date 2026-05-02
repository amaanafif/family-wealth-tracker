package com.familywealth.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FamilyWealthTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FamilyWealthTrackerApplication.class, args);
    }
}
