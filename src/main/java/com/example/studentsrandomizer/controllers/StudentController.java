package com.example.studentsrandomizer.controllers;

import com.example.studentsrandomizer.entity.Student;
import com.example.studentsrandomizer.utils.StudentPair;
import com.example.studentsrandomizer.service.RandomStudentService;
import com.example.studentsrandomizer.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final RandomStudentService randomService;
    Logger logger = LoggerFactory.getLogger("RandomizerLogger");

    public StudentController(StudentService studentService, RandomStudentService randomService) {
        this.studentService = studentService;
        this.randomService = randomService;
    }

    @GetMapping()
    public String allStudents(Model model) {
        Map<Integer, List<Student>> students = studentService.getStudents();
        model.addAttribute("students", students);
        return "students";
    }

    @GetMapping("/{id}")
    public String studentDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "student-details";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "student-edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") Student student, @PathVariable("id") Long id) {
        studentService.update(id, student);
        return "redirect:/students";
    }

    @GetMapping("/random-quiz")
    public String getRandomStudents(Model model) {
        StudentPair pair = randomService.loadStudentPairFromSave();

        if (pair == null) {
            return "quiz-not-started-yet";
        } else {

            model.addAttribute("studentPair", new StudentPair(pair.getPair()));
            model.addAttribute("studentPair", new StudentPair(pair.getPair()));
            return "random-quiz";
        }
    }

    @GetMapping("random-quiz/get-random-pair")
    public String getRandomPair() {
        randomService.collectStudentsToPair();
        return "redirect:/students/random-quiz";
    }

    @PostMapping("/set-score")
    public String nextPair(@ModelAttribute StudentPair pair, Model model) {
        model.addAttribute("studentPair", pair);
        studentService.addScore(pair);
        return "redirect:/students/random-quiz/get-random-pair";
    }

    @GetMapping("/reset-randomizing")
    public String resetRandomizing() {
        randomService.resetRandomizing();
        return "redirect:/students/random-quiz/get-random-pair";
    }

    @PostMapping()
    public String create(@ModelAttribute("newStudent") Student student) {
        studentService.save(student);
        return "redirect:/students";
    }

    @GetMapping("student-new")
    public String newStudent(@ModelAttribute("newStudent") Student student) {
        return "student-new";
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable("id") long id) {
        studentService.delete(id);
        return "redirect:/students";
    }
}
