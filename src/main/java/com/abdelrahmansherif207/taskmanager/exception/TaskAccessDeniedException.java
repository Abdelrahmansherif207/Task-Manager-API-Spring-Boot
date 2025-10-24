package com.abdelrahmansherif207.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TaskAccessDeniedException extends RuntimeException {
    public TaskAccessDeniedException(String message) {
        super(message);
    }
}