package com.digdes.school.repository;

import com.digdes.school.server.interfaces.MathOperationParser;

import java.util.Map;

public class MathematicalConverterRepository {
    private static MathematicalConverterRepository mathematicalConverterRepository;

    private MathematicalConverterRepository() {

    }

    public static MathematicalConverterRepository getInstance() {
        if (mathematicalConverterRepository == null) {
            mathematicalConverterRepository = new MathematicalConverterRepository();
        }
        return mathematicalConverterRepository;
    }

    private Map<String, MathOperationParser<Object, Boolean>> mathematicalSignsMap =
            Map.of("=",
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
                                String value = requestValueString.substring(requestValueString.indexOf("%") + 1, requestValueString.lastIndexOf("%"));
                                return currentValueString.contains(value);
                            }

                            if (requestValueString.matches("\\W+%+")) {
                                String value = requestValueString.substring(0, requestValueString.indexOf("%"));
                                return currentValueString.contains(value);
                            }
                            if (requestValueString.matches("%+\\W+")) {
                                String value = requestValueString.substring(requestValueString.indexOf("%") + 1);
                                return currentValueString.contains(value);
                            }


                        }
                        return false;
                    }
                    , ">", (currentValue, requestValue) -> {
                        if (currentValue instanceof Long) {
                            long currentValueLong = (long) currentValue;
                            long requestValueLong = (long) requestValue;
                            return currentValueLong > requestValueLong;
                        }
                        if (currentValue instanceof Double) {
                            double currentValueDouble = (double) currentValue;
                            double requestValueDouble = (double) requestValue;
                            return currentValueDouble > requestValueDouble;
                        }
                        return false;
                    }
                    , "<", (currentValue, requestValue) -> {
                        if (currentValue instanceof Long) {
                            long currentValueLong = (long) currentValue;
                            long requestValueLong = (long) requestValue;
                            return currentValueLong < requestValueLong;
                        }
                        if (currentValue instanceof Double) {
                            double currentValueDouble = (double) currentValue;
                            double requestValueDouble = (double) requestValue;
                            return currentValueDouble < requestValueDouble;
                        }
                        return false;
                    }
                    , ">=", (currentValue, requestValue) -> {
                        if (currentValue instanceof Long) {
                            long currentValueLong = (long) currentValue;
                            long requestValueLong = (long) requestValue;
                            return currentValueLong >= requestValueLong;
                        }
                        if (currentValue instanceof Double) {
                            double currentValueDouble = (double) currentValue;
                            double requestValueDouble = (double) requestValue;
                            return currentValueDouble >= requestValueDouble;
                        }
                        return false;
                    }
                    , "<=", (currentValue, requestValue) -> {
                        if (currentValue instanceof Long) {
                            long currentValueLong = (long) currentValue;
                            long requestValueLong = (long) requestValue;
                            return currentValueLong <= requestValueLong;
                        }
                        if (currentValue instanceof Double) {
                            double currentValueDouble = (double) currentValue;
                            double requestValueDouble = (double) requestValue;
                            return currentValueDouble <= requestValueDouble;
                        }
                        return false;
                    }
                    , "!=",
                    (currentValue, requestValue) -> {
                        if (currentValue instanceof Double) {
                            return (double) currentValue != (double) requestValue;
                        }
                        if (currentValue instanceof Long) {
                            return (long) currentValue != (long) requestValue;

                        }
                        if (currentValue instanceof String) {
                            return !String.valueOf(currentValue).equals(String.valueOf(requestValue));

                        }
                        if (currentValue instanceof Boolean) {
                            return (boolean) currentValue != (boolean) requestValue;
                        }


                        return false;
                    }
                    , "ilike", (currentValue, requestValue) -> {
                        if (currentValue instanceof String) {
                            String requestValueString = String.valueOf(requestValue).toLowerCase();
                            String currentValueString = String.valueOf(currentValue).toLowerCase();

                            if (!requestValueString.contains("%")) {
                                return currentValueString.contains(requestValueString);
                            }

                            if (requestValueString.matches("%+\\W+%+")) {
                                String value = requestValueString.substring(requestValueString.indexOf("%") + 1, requestValueString.lastIndexOf("%"));
                                return currentValueString.contains(value);
                            }

                            if (requestValueString.matches("\\W+%+")) {
                                String value = requestValueString.substring(0, requestValueString.indexOf("%"));
                                return currentValueString.contains(value);
                            }
                            if (requestValueString.matches("%+\\W+")) {
                                String value = requestValueString.substring(requestValueString.indexOf("%") + 1);
                                return currentValueString.contains(value);
                            }


                        }
                        return false;

                    });

    public Map<String, MathOperationParser<Object, Boolean>> getMathematicalSignsMap() {
        return mathematicalSignsMap;
    }
}
