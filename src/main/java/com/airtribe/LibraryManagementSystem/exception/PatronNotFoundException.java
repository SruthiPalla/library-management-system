package com.airtribe.LibraryManagementSystem.exception;

public class PatronNotFoundException extends RuntimeException {

    public PatronNotFoundException(String message) {
        super(message);
    }
}