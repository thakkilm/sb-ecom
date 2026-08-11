package com.ecommerce.project.sbecom.exceptions;

public class NoCategoriesPresentException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public NoCategoriesPresentException(String message) {
        super(message);
    }

    public NoCategoriesPresentException() {
    }
}