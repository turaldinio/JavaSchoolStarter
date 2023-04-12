package com.digdes.school.excption;

public class NonExistentParameter extends RuntimeException {

    public NonExistentParameter(String messge) {
        super(messge);
    }
}
