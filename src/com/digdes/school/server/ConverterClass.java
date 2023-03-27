package com.digdes.school.server;

import com.digdes.school.MathOperationParser;
import com.digdes.school.RequestParam;

import java.util.Map;
import java.util.function.Function;

public class ConverterClass {


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
    private static final Map<String, Function<String, ?>> convertionMap;
    private static Map<String, MathOperationParser<RequestParam<?>, Boolean>> operaionMap;
    private static Map<String, MathOperationParser<Object, Boolean>> timeMap;

    static {
        timeMap = Map.of("=",
                (currentValue, requestValue) -> {
                    if (currentValue instanceof Double) {
                        return (double) currentValue == (double) requestValue;
                    }
                    if (currentValue instanceof Long) {
                        return (long) currentValue == (long) requestValue;

                    }
                    if (currentValue instanceof String) {
                        return String.valueOf(currentValue).equals(String.valueOf(requestValue));

                    }
                    if (currentValue instanceof Boolean) {
                        return (boolean) currentValue == (boolean) requestValue;
                    }


                    return false;
                }
        );

        operaionMap = Map.of(
                "=", (currentValue, requestValue) ->

                {
                    if (currentValue.getValue() instanceof Double) {
                        return (double) currentValue.getValue() == (double) requestValue.getValue();
                    }
                    if (currentValue.getValue() instanceof Long) {
                        return (long) currentValue.getValue() == (long) requestValue.getValue();

                    }
                    if (currentValue.getValue() instanceof String) {
                        return String.valueOf(currentValue.getValue()).equals(String.valueOf(requestValue.getValue()));

                    }
                    if (currentValue.getValue() instanceof Boolean) {
                        return (boolean) currentValue.getValue() == (boolean) requestValue.getValue();
                    }


                    return false;
                }
        );

        convertionMap = Map.of(
                "id", transferToLong,
                "cost", transferToDouble,
                "lastname", transferToString,
                "active", transferToBoolean,
                "age", transferToLong

        );


    }

    public static Map<String, Function<String, ?>> getConvertionMap() {
        return convertionMap;
    }

    public static Map<String, MathOperationParser<RequestParam<?>, Boolean>> getOperaionMap() {
        return operaionMap;
    }

    public static Map<String, MathOperationParser<Object, Boolean>> getTimeMap() {
        return timeMap;
    }
}