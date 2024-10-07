package com.Uber.Project.UberApp.Advices;

import com.Uber.Project.UberApp.Exceptions.ResourceNotFoundException;
import com.Uber.Project.UberApp.Exceptions.RuntimeConflictException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>>HandleResponseNotFound(ResourceNotFoundException exception){
        ApiError error= ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build();

        return BuilderResponseErrorEntity(error);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>>HandleAuthenticationException(AuthenticationException exception){
        ApiError apiError=ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .build();

        return BuilderResponseErrorEntity(apiError);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>>HandleJwtException(JwtException exception){
        ApiError apiError= ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .build();

        return BuilderResponseErrorEntity(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>>HandleAccessDeniedException(AccessDeniedException exception){
        ApiError apiError=ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(exception.getMessage())
                .build();

        return BuilderResponseErrorEntity(apiError);
    }

    @ExceptionHandler(RuntimeConflictException.class)
    public ResponseEntity<ApiResponse<?>>HandleResponseConflict(RuntimeConflictException exception){
        ApiError error= ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message(exception.getMessage())
                .build();

        return BuilderResponseErrorEntity(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>>HandleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        ApiError apiError= ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();

        return BuilderResponseErrorEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>>HandleException(Exception exception){
        ApiError error= ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();

        return BuilderResponseErrorEntity(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>>HandleRuntimeException(RuntimeException exception){
        ApiError error=ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();

        return BuilderResponseErrorEntity(error);
    }

    private ResponseEntity<ApiResponse<?>>BuilderResponseErrorEntity(ApiError error){
        return new ResponseEntity<>(new ApiResponse<>(error),error.getStatus());
    }

}
