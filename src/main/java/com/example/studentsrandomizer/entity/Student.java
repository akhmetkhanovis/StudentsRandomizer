package com.example.studentsrandomizer.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "students")
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private boolean captain;

    @Column
    private double questionScore;

    @Column
    private double answerScore;

    @Transient
    private double totalScore = questionScore + answerScore;

    @Column
    private int team;

    public Student() {
    }

    public Student(Long id, String name, String surname, boolean isCaptain, int team) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.captain = isCaptain;
        this.team = team;
    }

    public Student(Student s) {
        this.id = s.getId();
        this.name = s.getName();
        this.surname = s.getSurname();
        this.captain = s.isCaptain();
        this.team = s.getTeam();
        this.questionScore += s.getQuestionScore();
        this.answerScore += s.getAnswerScore();
    }

    public boolean isCaptain() {
        return captain;
    }

    public void setCaptain(boolean captain) {
        this.captain = captain;
    }

    public double getScore() {
        return questionScore + answerScore;
    }

    @Override
    public String toString() {
        return surname + " " + name + ((captain) ? " (капитан)" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return captain == student.captain && team == student.team && id.equals(student.id) && name.equals(student.name) && surname.equals(student.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }

}
