package com.digdes.school.server;

import com.digdes.school.repository.ArgumentsTypesConverterRepository;

public class ArgumentsTypesConverterServer {
    private final ArgumentsTypesConverterRepository repository;

    public ArgumentsTypesConverterServer() {
        this.repository = new ArgumentsTypesConverterRepository();
    }

    public Object getTypedValue(String columnName, String columValue) {
        return repository.getConvertionMap().get(columnName).apply(columValue);

    }

}
