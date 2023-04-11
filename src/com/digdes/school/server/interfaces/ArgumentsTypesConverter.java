package com.digdes.school.server.interfaces;

@FunctionalInterface
public interface ArgumentsTypesConverter {
    Object getTypedValue(String columnName, String columValue);
}
