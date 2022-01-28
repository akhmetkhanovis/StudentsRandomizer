package com.example.studentsrandomizer.service;

import com.example.studentsrandomizer.entity.Student;
import com.example.studentsrandomizer.entity.StudentPair;
import com.example.studentsrandomizer.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private List<Student> forRandomizing = new ArrayList<>();
    private Student asking;
    private Student answering;
    private Student firstAsking;


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

    public StudentPair getRandomStudents() {
        int counter = 0;
        StudentPair pair = new StudentPair();

        if (forRandomizing.isEmpty()) {
            forRandomizing = studentRepository.findAll();
        }

        if (asking == null && firstAsking == null) {
            asking = forRandomizing.get(randomId());
            firstAsking = asking;
        }

        answering = forRandomizing.get(randomId());

        while (asking.getTeam() == answering.getTeam() || asking.equals(answering)) {
            answering = forRandomizing.get(randomId());

            if (counter > 2) {
                Set<Integer> teams = forRandomizing.stream()
                        .map(Student::getTeam)
                        .collect(Collectors.toSet());
                if (teams.size() < 2) {
                    if (!asking.equals(answering)) {
                        break;
                    } else if (forRandomizing.size() == 1 && asking.equals(answering)) {
                        break;
                    }
                }
            }
            counter++;
        }

        pair.addStudent(asking);
        pair.addStudent(answering);

        if (firstAsking.equals(answering)) {
            forRandomizing.remove(answering);
            asking = null;
            firstAsking = null;
        } else {
            asking = answering;
            forRandomizing.remove(answering);
        }

        return pair;
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

    private int randomId() {
        Random random = new Random();
        return random.nextInt(forRandomizing.size());
    }

    public void resetRandomizing() {
        forRandomizing = studentRepository.findAll();
    }

}
