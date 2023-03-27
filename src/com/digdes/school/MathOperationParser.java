package com.digdes.school;

@FunctionalInterface
public interface MathOperationParser<T, R> {
    R parseOperation(T t1, T t2);

}
