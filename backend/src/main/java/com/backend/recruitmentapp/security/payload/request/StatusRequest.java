package com.backend.recruitmentapp.security.payload.request;

/**
 *
 */
public class StatusRequest {
    private Integer status_id;
    private String status;
    private String person_id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public StatusRequest(Integer status_id, String status) {
        this.status_id = status_id;
        this.status = status;
    }
}
