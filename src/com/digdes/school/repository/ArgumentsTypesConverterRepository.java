package com.digdes.school.repository;

import java.util.Map;
import java.util.function.Function;

public class ArgumentsTypesConverterRepository {
    private static final Function<String, Long> transferToLong = Long::parseLong;
    private static final Function<String, Boolean> transferToBoolean = x -> {
        if (x.equals("true") || x.equals("false")) {
            return x.equals("true");
        }
        try {
            throw new Exception("class cast exception");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
    private static final Function<String, String> transferToString = x -> x;
    private static final Function<String, Double> transferToDouble = Double::parseDouble;
    private static final Map<String, Function<String, ?>> convertionMap = Map.of(
            "id", transferToLong,
            "cost", transferToDouble,
            "lastname", transferToString,
            "active", transferToBoolean,
            "age", transferToLong

    );

    public  Map<String, Function<String, ?>> getConvertionMap() {
        return convertionMap;
    }
}
