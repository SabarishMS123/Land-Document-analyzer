package com.landanalyzer.exception;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(Long id) {
        super("Land document not found with id: " + id);
    }
}
