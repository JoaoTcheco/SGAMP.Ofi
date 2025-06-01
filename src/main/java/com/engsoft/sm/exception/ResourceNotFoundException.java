package com.engsoft.sm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção customizada para ser lançada quando um recurso específico não é encontrado.
 * A anotação @ResponseStatus faz com que o Spring retorne o código HTTP especificado
 * quando esta exceção não é tratada por um @ExceptionHandler.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND) // Retorna HTTP 404 Not Found
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L; // Para serialização

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s não encontrado com %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
