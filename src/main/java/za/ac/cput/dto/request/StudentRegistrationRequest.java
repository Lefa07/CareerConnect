package za.ac.cput.dto.request;

/*
 * StudentRegistrationRequest.java
 * Request DTO for student registration
 * Author: Ebenezer Kouakou (230480152)
 * Date: 17 October 2026
 */
public record StudentRegistrationRequest(
        String studentNumber,
        String name,
        String password,
        String confirmPassword
) {}