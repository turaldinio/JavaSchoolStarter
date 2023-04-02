package com.digdes.school.server;

import com.digdes.school.excption.NonExistentParameter;
import com.digdes.school.repository.JavaSchoolRepository;

import java.util.*;


public class JavaSchoolServer {
    private static final int MATH_OPERATION = 0;
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_VALUE = 2;
    private JavaSchoolRepository javaSchoolRepository;

    public JavaSchoolServer() {
        this.javaSchoolRepository = new JavaSchoolRepository();
    }

    public List<Map<String, Object>> insert(String request) {
        var map = new HashMap<String, Object>();
        String stub = "insert values";

        try {
            var array = request.substring(stub.length()).split(",");

            for (String line : array) {
                String cleanParameters = line.replaceAll("'", "").trim();

                var processedRequestData = getProcessedRequestData(cleanParameters);
                var typedObject = ConverterClass.getConvertionMap().get(processedRequestData[COLUMN_NAME]).
                        apply(processedRequestData[COLUMN_VALUE]);

                map.put(processedRequestData[COLUMN_NAME], typedObject);

            }
            return javaSchoolRepository.insert(map);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Map<String, Object>> update(String request) {
        String stub = "update values";
        request = request.replaceAll("'", "");

        String filterCondition = request.
                substring(request.
                        indexOf("where") + "where".
                        length());

        String[] newValues = request.substring(stub.length(), request.indexOf("where")).split(",");
        String[] filterConditionArray = null;
        boolean greedy = false;

        if (filterCondition.contains("or") && !filterCondition.contains("and")) {
            filterConditionArray = filterCondition.split("or");

        }
        if (filterCondition.contains("and") && !filterCondition.contains("or")) {
            filterConditionArray = filterCondition.split("and");
            greedy = true;
        }
        var result = filterTheCollection(filterConditionArray, newValues, greedy);

        return javaSchoolRepository.update(updateValuesInRepository(newValues, result));

    }

    public boolean checkingValidityOfValues(String[] filterArray, Map<String, Object> map, boolean greedy) {
        for (String currentFilter : filterArray) {

            var parseRequestParameters = getProcessedRequestData(currentFilter);

            var repositoryValue = ConverterClass.getConvertionMap().get(parseRequestParameters[COLUMN_NAME]).
                    apply(String.valueOf(map.get(parseRequestParameters[COLUMN_NAME])));

            var requestValue = ConverterClass.getConvertionMap().get(parseRequestParameters[COLUMN_NAME]).
                    apply(parseRequestParameters[COLUMN_VALUE]);


            if (!ConverterClass.getMathematicalSignsMap().get(parseRequestParameters[MATH_OPERATION]).parseOperation(repositoryValue, requestValue) &&
                    greedy) {
                return false;
            }
            if (ConverterClass.getMathematicalSignsMap().get(parseRequestParameters[MATH_OPERATION]).parseOperation(repositoryValue, requestValue) &&
                    !greedy) {
                return true;
            }


        }
        return greedy;

    }


    public List<Map<String, Object>> filterTheCollection(String[] requestLine, String[] newValues, boolean greedy) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        Iterator<Map<String, Object>> iterator = javaSchoolRepository.getIterator();

        while (iterator.hasNext()) {
            Map<String, Object> currentMap = iterator.next();

            if (checkAvailabilityOfAllKeys(requestLine, currentMap) && checkAvailabilityOfAllKeys(newValues, currentMap)) {

                if (checkingValidityOfValues(requestLine, currentMap, greedy)) {
                    mapList.add(currentMap);
                    javaSchoolRepository.deleteMap(iterator);
                }

            }
        }

        return mapList;
    }


    public List<Map<String, Object>> select(String request) {
        return javaSchoolRepository.select(request);


    }

    public List<Map<String, Object>> delete(String request) {
        return javaSchoolRepository.delete(request);


    }

    public List<Map<String, Object>> updateValuesInRepository(String[] newValues, List<Map<String, Object>> map) {
        for (Map<String, Object> pairs : map) {
            Arrays.stream(newValues).
                    map(this::getProcessedRequestData).
                    forEach(x -> pairs.put(x[COLUMN_NAME],
                            ConverterClass.getConvertionMap().
                                    get(x[COLUMN_NAME]).
                                    apply(x[COLUMN_VALUE])));
        }


        return map;
    }

    public boolean checkAvailabilityOfAllKeys(String[] request, Map<String, Object> map) {
        for (String parameter : request) {
            var paramArray = getProcessedRequestData(parameter);
            if (!map.containsKey(paramArray[COLUMN_NAME])) {
                throw new NonExistentParameter(String.format("the '%s' parameter is not found", paramArray[COLUMN_NAME]));

            }
        }
        return true;

    }

    public String[] getProcessedRequestData(String data) {
        String[] array = new String[MATH_OPERATION + COLUMN_NAME + COLUMN_VALUE];
        array[MATH_OPERATION] = data.replaceAll("[^!=><%]", "");
        array[COLUMN_NAME] = data.substring(MATH_OPERATION, data.indexOf(array[MATH_OPERATION])).trim();
        array[COLUMN_VALUE] = data.substring(data.indexOf(array[MATH_OPERATION]) + COLUMN_NAME).trim();
        return array;
    }
}
