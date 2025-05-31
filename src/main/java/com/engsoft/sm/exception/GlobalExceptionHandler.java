package com.engsoft.sm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        // Você pode adicionar mais detalhes como o tipo de recurso, etc.
        return "error/404"; // Uma view error/404.html
    }

    @ExceptionHandler(Exception.class) // Handler genérico para outras exceções
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Ocorreu um erro inesperado no sistema.");
        // Logar a exceção exaustivamente aqui: log.error("Erro inesperado", ex);
        return "error/500"; // Uma view error/500.html
    }
}