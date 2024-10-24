package com.example.date_values;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DateValuesApplication {

    public static void main(String[] args) {
        SpringApplication.run(DateValuesApplication.class, args);
    }

}
