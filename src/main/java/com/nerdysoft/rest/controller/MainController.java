package com.nerdysoft.rest.controller;

import com.nerdysoft.rest.error.DatabaseOperationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> handleDatabaseIntegrityException() {
        return ResponseEntity.badRequest().body("Operation is forbidden: Primary key of record is used in another table");
    }

}
