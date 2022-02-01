package com.example.studentsrandomizer.utils;

import com.example.studentsrandomizer.entity.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StudentPair {
    private List<Student> pair;

    public StudentPair() {
        this.pair = new ArrayList<>();
    }

    public StudentPair(List<Student> pair) {
        this.pair = pair;
    }

    public void addStudent(Student student) {
        pair.add(student);
    }

    public StudentPairSave save() {
        return new StudentPairSave(pair);
    }

    public StudentPair load(StudentPairSave save) {
        if (save == null) return null;
        this.pair = save.getPair();
        return this;
    }
}
