package ru.job4j.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class.getSimpleName());
    private ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public String handlerNotFound(TaskNotFoundException e, Model model) {
        log.error(e.getMessage(), e);
        model.addAttribute("message", e.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(TaskUpdateException.class)
    public String handleUpdate(TaskUpdateException e, Model model) {
        log.error(e.getMessage(), e);
        model.addAttribute("message", e.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException e, Model model) {
        log.info(e.getMessage(), e);
        model.addAttribute("message", e.getMessage());
        return "errors/401";
    }

    @ExceptionHandler(DuplicateLoginException.class)
    public String handleLoginDuplication(DuplicateLoginException e, Model model) {
        log.warn(e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "errors/409";
    }
}
