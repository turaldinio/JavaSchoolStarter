package com.digdes.school.server;

import com.digdes.school.MathOperationParser;

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
    private static Map<String, MathOperationParser<Object, Boolean>> mathematicalSignsMap;

    static {
        mathematicalSignsMap = Map.of("=",
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
                , "like", (currentValue, requestValue) -> {
                    if (currentValue instanceof String) {
                        String requestValueString = String.valueOf(requestValue);
                        String currentValueString = String.valueOf(currentValue);

                        if (!requestValueString.contains("%")) {
                            return currentValueString.contains(requestValueString);
                        }

                        if (requestValueString.matches("%+\\W+%+")) {
                            String value = requestValueString.substring(requestValueString.indexOf("%")+1, requestValueString.lastIndexOf("%"));
                            return currentValueString.contains(value);
                        }

                        if (requestValueString.matches("\\W+%+")) {
                            String value = requestValueString.substring(0, requestValueString.indexOf("%"));
                            return currentValueString.contains(value);
                        }
                        if (requestValueString.matches("%+\\W+")) {
                            String value = requestValueString.substring(requestValueString.indexOf("%")+1);
                            return currentValueString.contains(value);
                        }



                    }
                    return false;
                })


        ;

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


    public static Map<String, MathOperationParser<Object, Boolean>> getMathematicalSignsMap() {
        return mathematicalSignsMap;
    }
}