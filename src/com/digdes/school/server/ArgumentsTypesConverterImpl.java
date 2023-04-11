package com.digdes.school.server;

import com.digdes.school.repository.ArgumentsTypesConverterRepository;
import com.digdes.school.server.interfaces.ArgumentsTypesConverter;

public class ArgumentsTypesConverterImpl implements ArgumentsTypesConverter {
    private static ArgumentsTypesConverterImpl argumentsTypesConverter;

    private final ArgumentsTypesConverterRepository repository;

    private ArgumentsTypesConverterImpl() {
        repository = ArgumentsTypesConverterRepository.getInstance();
    }

    public static ArgumentsTypesConverterImpl getInstance() {
        if (argumentsTypesConverter == null) {
            argumentsTypesConverter = new ArgumentsTypesConverterImpl();
        }
        return argumentsTypesConverter;
    }


    @Override
    public Object getTypedValue(String columnName, String columValue) {
        return repository.getConvertionMap().get(columnName).apply(columValue);

    }
}
