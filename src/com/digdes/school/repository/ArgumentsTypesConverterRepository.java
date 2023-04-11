package com.digdes.school.repository;

import java.util.Map;
import java.util.function.Function;

public class ArgumentsTypesConverterRepository {
    private static ArgumentsTypesConverterRepository argumentsTypesConverterRepository;

    private ArgumentsTypesConverterRepository() {

    }

    public static ArgumentsTypesConverterRepository getInstance() {
        if (argumentsTypesConverterRepository == null) {
            argumentsTypesConverterRepository = new ArgumentsTypesConverterRepository();
        }
        return argumentsTypesConverterRepository;
    }

    private static final Function<String, Long> transferToLong = x -> x.contains("null") ? null : Long.parseLong(x);
    private static final Function<String, Boolean> transferToBoolean = x -> {
        if (x.equals("true") || x.equals("false")) {
            return x.equals("true");
        }
        if (x.equals("null")) {
            return null;
        }
        try {
            throw new Exception("class cast exception");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
    private static final Function<String, String> transferToString = x -> x;
    private static final Function<String, Double> transferToDouble = x -> x.equals("null") ? null : Double.parseDouble(x);
    private static final Map<String, Function<String, ?>> convertionMap = Map.of(
            "id", transferToLong,
            "cost", transferToDouble,
            "lastname", transferToString,
            "active", transferToBoolean,
            "age", transferToLong

    );

    public Map<String, Function<String, ?>> getConvertionMap() {
        return convertionMap;
    }
}
