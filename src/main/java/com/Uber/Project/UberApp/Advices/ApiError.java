package com.Uber.Project.UberApp.Advices;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@Data
public class ApiError {

    HttpStatus status;
    String message;
}
