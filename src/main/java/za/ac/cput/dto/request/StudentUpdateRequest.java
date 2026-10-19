package za.ac.cput.dto.request;

/*
 * StudentUpdateRequest.java
 * Request DTO for updating student profile
 * Author: Ebenezer Kouakou (230480152)
 * Date: 17 October 2026
 */
public record StudentUpdateRequest(
        String name,
        String email
) {}