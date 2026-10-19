package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Student;
import za.ac.cput.dto.request.StudentRegistrationRequest;
import za.ac.cput.dto.request.StudentUpdateRequest;
import za.ac.cput.dto.response.StudentResponse;
import za.ac.cput.factory.StudentFactory;
import za.ac.cput.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

/*
 * StudentService.java
 * Student Service Implementation
 * Author: Ebenezer Kouakou (230480152)
 * Date: 17 October 2026
 */

@Service
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentResponse create(StudentRegistrationRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        String cputEmail = request.studentNumber().trim() + "@mycput.ac.za";

        Student student = StudentFactory.buildStudent(
                request.studentNumber().trim(),
                request.name().trim(),
                cputEmail,
                request.password()
        );

        if (student == null) {
            throw new IllegalArgumentException("Invalid registration input fields.");
        }

        if (studentRepository.existsById(student.getStudentNumber())) {
            throw new IllegalArgumentException("Student number already registered.");
        }

        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use.");
        }

        Student saved = studentRepository.save(student);
        return StudentResponse.fromEntity(saved);
    }

    @Override
    public StudentResponse read(String studentNumber) {
        return studentRepository.findById(studentNumber)
                .map(StudentResponse::fromEntity)
                .orElse(null);
    }

    @Override
    public StudentResponse update(String studentNumber, StudentUpdateRequest request) {
        Student existing = studentRepository.findById(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with number: " + studentNumber));

        Student updated = new Student.Builder()
                .copy(existing)
                .setName(request.name().trim())
                .setEmail(request.email().trim())
                .build();

        Student saved = studentRepository.save(updated);
        return StudentResponse.fromEntity(saved);
    }

    @Override
    public boolean delete(String studentNumber) {
        if (studentRepository.existsById(studentNumber)) {
            studentRepository.deleteById(studentNumber);
            return true;
        }
        return false;
    }

    @Override
    public List<StudentResponse> getAll() {
        return studentRepository.findAll()
                .stream()
                .map(StudentResponse::fromEntity)
                .toList();
    }

    @Override
    public Optional<StudentResponse> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .map(StudentResponse::fromEntity);
    }

    @Override
    public List<StudentResponse> getStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(StudentResponse::fromEntity)
                .toList();
    }

    @Override
    public StudentResponse authenticate(String identifier, String password) {
        String searchEmail = identifier.trim().contains("@")
                ? identifier.trim()
                : identifier.trim() + "@mycput.ac.za";

        Student student = studentRepository.findByEmail(searchEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student credentials."));

        if (!student.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid student credentials.");
        }

        return StudentResponse.fromEntity(student);
    }
}