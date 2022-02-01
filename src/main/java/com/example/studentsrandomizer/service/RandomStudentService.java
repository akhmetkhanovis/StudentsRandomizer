package com.example.studentsrandomizer.service;

import com.example.studentsrandomizer.entity.Student;
import com.example.studentsrandomizer.utils.StudentPair;
import com.example.studentsrandomizer.repository.StudentRepository;
import com.example.studentsrandomizer.utils.StudentPairSave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RandomStudentService {
    private final StudentRepository studentRepository;
    private Student asking;
    private Student firstAsking;
    private Student answering;
    private List<Student> forRandomizing = new ArrayList<>();
    StudentPair pair;
    private StudentPairSave save;
    Logger logger = LoggerFactory.getLogger("RandomizerLogger");

    public RandomStudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentPair collectStudentsToPair() {
        if (forRandomizing.isEmpty()) {
            resetRandomizing();
        }

        if (asking == null && firstAsking == null) {
            asking = forRandomizing.get(randomId());
            firstAsking = asking;
            answering = forRandomizing.get(randomId());
            answering = getRandomAnsweringStudent(asking, answering);
            pair = createStudentPair(asking, answering);
            forRandomizing.remove(answering);
            save = pair.save();
            logger.info("Задает вопрос: " + pair.getPair().get(0)
                    + " <<<<>>>> Отвечает: " + pair.getPair().get(1));
            return pair;
        }

        asking = answering;
        answering = getRandomAnsweringStudent(asking, answering);
        pair = createStudentPair(asking, answering);
        save = pair.save();

        if (firstAsking.equals(answering)) {
            forRandomizing.remove(answering);
            asking = null;
            firstAsking = null;
        } else {
            asking = answering;
            forRandomizing.remove(answering);
        }

        logger.info("Задает вопрос: " + pair.getPair().get(0)
                + " <<<<>>>> Отвечает: " + pair.getPair().get(1));
        return pair;
    }

    public StudentPair loadStudentPairFromSave() {
        return new StudentPair().load(save);
    }

    private StudentPair createStudentPair(Student asking, Student answering) {
        StudentPair pair = new StudentPair();
        pair.getPair().clear();
        pair.addStudent(asking);
        pair.addStudent(answering);
        return pair;
    }

    private Student getRandomAnsweringStudent(Student asking, Student answering) {
        int counter = 0;
        while (asking.getTeam() == answering.getTeam() || asking.equals(answering)) {
            answering = forRandomizing.get(randomId());

            if (counter > 2) {
                Set<Integer> teams = forRandomizing.stream()
                        .map(Student::getTeam)
                        .collect(Collectors.toSet());
                if (teams.size() < 2 && !asking.equals(answering)) {
                    break;
                } else if (forRandomizing.size() == 1 && asking.equals(answering)) {
                    break;
                }
            }
            counter++;
        }

        return answering;
    }

    private int randomId() {
        Random random = new Random();
        return random.nextInt(forRandomizing.size());
    }

    public void resetRandomizing() {
        logger.info("**********Start randomizing**********");
        firstAsking = null;
        asking = null;
        answering = null;
        forRandomizing = studentRepository.findAll();
    }
}
