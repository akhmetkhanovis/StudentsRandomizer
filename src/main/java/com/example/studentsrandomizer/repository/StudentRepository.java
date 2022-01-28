package com.example.studentsrandomizer.repository;

import com.example.studentsrandomizer.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByTeam(int team);
}
