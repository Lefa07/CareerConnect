import apiClient from './apiClient';

/*
 * applicationService.js
 * Consuming Spring Boot Application REST APIs
 */

export const applyForInternship = async (studentNumber, internshipId) => {
    try {
        const response = await apiClient.post('/application/create', {
            studentNumber,
            internshipId
        });
        return response.data;
    } catch (error) {
        console.error("Error applying for internship:", error);
        throw error.response?.data || "Failed to submit application.";
    }
};

export const getStudentApplications = async (studentNumber) => {
    try {
        const response = await apiClient.get(`/application/student/${studentNumber}`);
        return response.data;
    } catch (error) {
        console.error("Error fetching student applications:", error);
        throw error;
    }
};

export const getInternshipApplicants = async (internshipId) => {
    try {
        const response = await apiClient.get(`/application/internship/${internshipId}`);
        return response.data;
    } catch (error) {
        console.error("Error fetching applicants:", error);
        throw error;
    }
};

export const updateApplicationStatus = async (applicationId, status) => {
    try {
        const response = await apiClient.put(`/application/update/${applicationId}?status=${status}`);
        return response.data;
    } catch (error) {
        console.error("Error updating application status:", error);
        throw error;
    }
};