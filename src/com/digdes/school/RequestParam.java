package com.digdes.school;

public class RequestParam<T> {
    private T value;


    public RequestParam() {

    }

    public RequestParam(T value) {
        this.value = value;
    }


    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
