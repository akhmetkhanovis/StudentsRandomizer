package com.example.studentsrandomizer.service;

import com.example.studentsrandomizer.entity.Student;
import com.example.studentsrandomizer.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    public TreeMap<Integer, List<Student>> getStudents() {
        List<Student> studentsList = studentRepository.findAll();
        Map<Integer, List<Student>> collect = studentsList.stream()
                .collect(Collectors.groupingBy(Student::getTeam, Collectors.toList()));
        return new TreeMap<>(collect);
    }

}
