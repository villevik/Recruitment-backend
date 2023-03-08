package com.backend.recruitmentapp.security.payload.request;

/**
 * Represents a parameter for the changeStatus function in the ApplicationController.
 */
public class AvailabilityRequest {
    private Integer availability_id;
    private String status;

    public Integer getAvailability_id() {
        return availability_id;
    }

    public void setAvailability_id(Integer availability_id) {
        this.availability_id = availability_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
