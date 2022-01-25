package com.example.studentsrandomizer;

import com.example.studentsrandomizer.repository.StudentRepository;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentsRandomizerApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(StudentsRandomizerApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run();
    }

}
