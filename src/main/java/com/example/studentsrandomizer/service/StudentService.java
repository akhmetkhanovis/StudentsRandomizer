package com.example.studentsrandomizer.service;

import com.example.studentsrandomizer.entity.Student;
import com.example.studentsrandomizer.utils.StudentPair;
import com.example.studentsrandomizer.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void update(Long id, Student student) {
        Student studentToBeUpdated = new Student(student);
        studentRepository.save(studentToBeUpdated);

        if (student.isCaptain()) {
            List<Student> team = studentRepository.findAllByTeam(studentToBeUpdated.getTeam());
            team.remove(studentToBeUpdated);
            team.forEach(s -> s.setCaptain(false));
            studentRepository.saveAll(team);
        }
    }

    public Map<Integer, List<Student>> getStudents() {
        return studentRepository.findAll().stream()
                .collect(Collectors.groupingBy(Student::getTeam, TreeMap::new, Collectors.toList()));
    }

    public void addScore(StudentPair pair) {
        Student stAsking = pair.getPair().get(0);
        Student stAnswering = pair.getPair().get(1);

        Student studentToBeUpdatedAsking = studentRepository.findById(stAsking.getId()).get();
        studentToBeUpdatedAsking.setQuestionScore(studentToBeUpdatedAsking.getQuestionScore() + stAsking.getQuestionScore());
        studentToBeUpdatedAsking.setAnswerScore(studentToBeUpdatedAsking.getAnswerScore() + stAsking.getAnswerScore());
        studentRepository.save(studentToBeUpdatedAsking);

        Student studentToBeUpdatedAnswering = studentRepository.findById(stAnswering.getId()).get();
        studentToBeUpdatedAnswering.setQuestionScore(studentToBeUpdatedAnswering.getQuestionScore() + stAnswering.getQuestionScore());
        studentToBeUpdatedAnswering.setAnswerScore(studentToBeUpdatedAnswering.getAnswerScore() + stAnswering.getAnswerScore());
        studentRepository.save(studentToBeUpdatedAnswering);
    }

    public void save(Student student) {
        studentRepository.save(student);
    }

    public void delete(long id) {
        studentRepository.deleteById(id);
    }
}
