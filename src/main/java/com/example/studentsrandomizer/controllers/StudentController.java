package com.example.studentsrandomizer.controllers;

import com.example.studentsrandomizer.entity.Student;
import com.example.studentsrandomizer.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
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
}
