package com.example.studentsrandomizer.service;

import com.example.studentsrandomizer.entity.Student;
import com.example.studentsrandomizer.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private List<Student> forRandomizing = new ArrayList<>();
    private Student studentAsking;
    private Student firstStudentAsking;


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

    public Student[] getRandomStudents() {
        Student[] pairOfStudents = new Student[2];

        if (forRandomizing.isEmpty()) {
            System.out.println("-------------------------студенты всё-------------------------");
            forRandomizing = studentRepository.findAll();
            studentAsking = null;
            firstStudentAsking = null;
        }

        if (studentAsking == null) {
            studentAsking = forRandomizing.get(getRandomId());
            firstStudentAsking = studentAsking;
        }

        Student studentAnswering = forRandomizing.get(getRandomId());

        if (forRandomizing.size() <= 4) {
            pairOfStudents[1] = studentAnswering;
        } else {
            while (studentAsking.getTeam() == studentAnswering.getTeam()) {
                studentAnswering = forRandomizing.get(getRandomId());
            }
        }

        pairOfStudents[0] = (studentAsking.equals(firstStudentAsking)) ? forRandomizing.get(getRandomId()) : studentAsking; // переделать
        pairOfStudents[1] = studentAnswering;

        studentAsking = studentAnswering;

        forRandomizing.remove(studentAnswering);

        return pairOfStudents;
    }

    private int getRandomId() {
        Random random = new Random();
        return random.nextInt(forRandomizing.size());
    }

    public void resetRandomizing() {
        forRandomizing = studentRepository.findAll();
    }

}
