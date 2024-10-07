package com.Uber.Project.UberApp.Advices;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<t> {

    private t data;

    private ApiError error;

    private LocalDateTime timestamp;

    public ApiResponse(t data) {
        this();
        this.data = data;

    }

    public ApiResponse(ApiError error) {
        this();
        this.error=error;
    }

    public ApiResponse() {
        timestamp = LocalDateTime.now();

    }
}
