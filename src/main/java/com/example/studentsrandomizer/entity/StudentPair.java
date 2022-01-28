package com.example.studentsrandomizer.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StudentPair {
    private List<Student> pair;

    public StudentPair() {
        this.pair = new ArrayList<>(2);
    }

    public StudentPair(List<Student> pair) {
        this.pair = pair;
    }

    public void addStudent(Student student) {
        pair.add(student);
    }
}
