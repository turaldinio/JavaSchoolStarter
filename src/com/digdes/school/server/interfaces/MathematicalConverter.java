package com.digdes.school.server.interfaces;
@FunctionalInterface
public interface MathematicalConverter {
    Boolean isTheDateCorrect(String mathematicalSymbol, Object repositoryValue, Object requestValue);

}
