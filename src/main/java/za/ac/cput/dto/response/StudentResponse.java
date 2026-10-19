package za.ac.cput.dto.response;

import za.ac.cput.domain.Student;

/*
 * StudentResponse.java
 * Safe Response DTO omitting sensitive credentials
 * Author: Ebenezer Kouakou (230480152)
 * Date: 17 October 2026
 */
public record StudentResponse(
        String studentNumber,
        String name,
        String email
) {
    public static StudentResponse fromEntity(Student student) {
        if (student == null) return null;
        return new StudentResponse(
                student.getStudentNumber(),
                student.getName(),
                student.getEmail()
        );
    }
}