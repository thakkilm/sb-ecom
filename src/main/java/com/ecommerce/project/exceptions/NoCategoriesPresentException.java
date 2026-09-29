package com.ecommerce.project.exceptions;

public class NoCategoriesPresentException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public NoCategoriesPresentException(String message) {
        super(message);
    }

    public NoCategoriesPresentException() {
    }
}