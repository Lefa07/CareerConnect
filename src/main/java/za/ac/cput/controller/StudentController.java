package za.ac.cput.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.dto.request.StudentRegistrationRequest;
import za.ac.cput.dto.request.StudentUpdateRequest;
import za.ac.cput.dto.response.StudentResponse;
import za.ac.cput.service.IStudentService;

import java.util.List;

/*
 * StudentController.java
 * REST API for Student operations using safe DTO contracts
 * Author: Ebenezer Kouakou (230480152)
 * Date: 17 October 2026
 */

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    private final IStudentService studentService;

    public StudentController(IStudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody StudentRegistrationRequest request) {
        try {
            StudentResponse response = studentService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/read/{studentNumber}")
    public ResponseEntity<StudentResponse> read(@PathVariable String studentNumber) {
        StudentResponse response = studentService.read(studentNumber);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{studentNumber}")
    public ResponseEntity<?> update(@PathVariable String studentNumber, @RequestBody StudentUpdateRequest request) {
        try {
            StudentResponse updated = studentService.update(studentNumber, request);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{studentNumber}")
    public ResponseEntity<Void> delete(@PathVariable String studentNumber) {
        boolean deleted = studentService.delete(studentNumber);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<StudentResponse>> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<StudentResponse> getByEmail(@RequestParam String email) {
        return studentService.getStudentByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/getByName")
    public ResponseEntity<List<StudentResponse>> getByName(@RequestParam String name) {
        return ResponseEntity.ok(studentService.getStudentsByName(name));
    }

    public record LoginRequest(String identifier, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            StudentResponse response = studentService.authenticate(request.identifier(), request.password());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}