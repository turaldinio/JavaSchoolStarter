package com.digdes.school.server;

import com.digdes.school.repository.ArgumentsTypesConverterRepository;
import com.digdes.school.server.interfaces.ArgumentsTypesConverter;

public class ArgumentsTypesConverterImpl implements ArgumentsTypesConverter {
    private final ArgumentsTypesConverterRepository repository;

    public ArgumentsTypesConverterImpl() {
        this.repository = new ArgumentsTypesConverterRepository();
    }


    @Override
    public Object getTypedValue(String columnName, String columValue) {
        return repository.getConvertionMap().get(columnName).apply(columValue);

    }
}
