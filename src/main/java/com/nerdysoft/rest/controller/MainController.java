package com.nerdysoft.rest.controller;

import com.nerdysoft.rest.error.DatabaseOperationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ExceptionHandler(DatabaseOperationException.class)
    @ResponseBody
    public ResponseEntity<String> handleDatabaseException(DatabaseOperationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<String> handleDatabaseException() {
        return ResponseEntity.badRequest().body("Insert operation is forbidden");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<String> handleDatabaseException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getConstraintViolations().iterator().next().getMessage());
    }
}
