package com.digdes.school;

public class RequestParam<T> {
    private String name;
    private T value;

    public RequestParam(String name, T value) {
        this.name = name;
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
