package com.digdes.school.excption;

public class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super(message);
    }
}
