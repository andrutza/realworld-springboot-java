package com.example.webdemo.exception;

import com.example.webdemo.dto.response.error.CustomErrorResponse;
import com.example.webdemo.dto.response.error.ErrorModel;
import com.example.webdemo.dto.response.error.ValidationErrorResponse;
import com.example.webdemo.dto.response.error.ValidationErrors;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ErrorModel> errorModelList = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorModel(error.getField(), error.getRejectedValue(), error.getDefaultMessage()))
                .distinct()
                .collect(Collectors.toList());
        ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
                .errors(ValidationErrors.builder()
                        .body(errorModelList)
                        .build())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = SQLException.class)
    public ResponseEntity<CustomErrorResponse> handleGenericSQLException(SQLException e) {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .errorCode("SQL_ERROR")
                .errorMsg(e.getMessage())
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageConversionException.class)
    public ResponseEntity<CustomErrorResponse> handleHttpMessageConversionException(HttpMessageConversionException e) {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .errorCode("JSON_ERROR")
                .errorMsg(e.getMessage())
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFoundException() {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .errorCode("NOT_FOUND_ERROR")
                .errorMsg("Resource not found")
                .timestamp(new Date())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ItemAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleAlreadyExistsException(ItemAlreadyExistsException exception) {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .errorCode("ALREADY_EXISTS_ERROR")
                .errorMsg(exception.getMessage())
                .timestamp(new Date())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = ItemNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNotFoundException(ItemNotFoundException exception) {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .errorCode("NOT_FOUND_ERROR")
                .errorMsg(exception.getMessage())
                .timestamp(new Date())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity<CustomErrorResponse> handleJwtException(JwtException exception) {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .errorCode("EXPIRED_JWT_ERROR")
                .errorMsg(exception.getMessage())
                .timestamp(new Date())
                .status(HttpStatus.FORBIDDEN.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
