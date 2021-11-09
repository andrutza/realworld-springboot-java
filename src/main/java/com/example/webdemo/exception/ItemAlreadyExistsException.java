package com.example.webdemo.exception;

public class ItemAlreadyExistsException extends RuntimeException {

    public ItemAlreadyExistsException(String message) {
        super(message);
    }
}
