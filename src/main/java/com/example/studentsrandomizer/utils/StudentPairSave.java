package com.example.studentsrandomizer.utils;

import com.example.studentsrandomizer.entity.Student;
import lombok.Getter;

import java.util.List;

@Getter
public class StudentPairSave {
    private final List<Student> pair;

    public StudentPairSave(List<Student> pair) {
        this.pair = pair;
    }
}
