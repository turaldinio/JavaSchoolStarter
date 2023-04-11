package com.digdes.school.server;

import com.digdes.school.excption.NonExistentParameter;
import com.digdes.school.repository.JavaSchoolRepository;
import com.digdes.school.server.interfaces.ArgumentsTypesConverter;
import com.digdes.school.server.interfaces.MathematicalConverter;

import java.util.*;


public class JavaSchoolServer {
    private static final int MATH_OPERATION = 1;
    private static final int COLUMN_NAME = 0;
    private static final int COLUMN_VALUE = 2;
    private final JavaSchoolRepository javaSchoolRepository;
    private final SortStation sortStation;
    private final ArgumentsTypesConverter argumentsConverterServer;

    private final MathematicalConverter mathematicalConverterServer;

    public JavaSchoolServer() {
        this.javaSchoolRepository = new JavaSchoolRepository();
        this.sortStation = new SortStation(this);
        this.argumentsConverterServer = new ArgumentsTypesConverterImpl();
        mathematicalConverterServer = new MathematicalConverterImpl();
    }

    public List<Map<String, Object>> insert(String request) {
        var map = new HashMap<String, Object>();
        String stub = "insert values";

        try {
            var array = request.substring(stub.length()).split(",");

            for (String line : array) {
                String cleanParameters = line.replaceAll("'", "");

                var processedRequestData = getProcessedRequestData(cleanParameters);
                var typedObject = argumentsConverterServer.getTypedValue(processedRequestData[COLUMN_NAME], processedRequestData[COLUMN_VALUE]);

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

        String filterCondition = request.
                substring(request.
                        indexOf("where") + "where".
                        length());

        String[] newValues = request.substring(stub.length(), request.indexOf("where")).split(",");

        var timeResult = findASuitableCollection(filterCondition, newValues);

        return javaSchoolRepository.update(updateValuesInRepository(newValues, timeResult));

    }

    public List<Map<String, Object>> findASuitableCollection(String filterCondition, String[] newValues) {
        String[] filterConditionArray = null;
        boolean greedy = false;
        List<Map<String, Object>> result = null;


        if (!filterCondition.contains("and") && !filterCondition.contains("or")) {
            filterConditionArray = parseSingletonRequest(filterCondition);
            greedy = true;

            result = filterTheCollection(filterConditionArray, newValues, greedy);

        } else {
            result = sortStation.calculatePostfixRequest(sortStation.getPostfixRequest(filterCondition));
        }

        return result;

    }

    public String[] parseSingletonRequest(String request) {
        return request.split("\n");
    }


    public boolean checkingValidityOfValues(String[] filterArray, Map<String, Object> map, boolean greedy) {
        for (String currentFilter : filterArray) {

            var parseRequestParameters = getProcessedRequestData(currentFilter);

            var repositoryValue = argumentsConverterServer.getTypedValue(parseRequestParameters[COLUMN_NAME],
                    String.valueOf(map.get(parseRequestParameters[COLUMN_NAME])));

            var requestValue = argumentsConverterServer.getTypedValue(parseRequestParameters[COLUMN_NAME], parseRequestParameters[COLUMN_VALUE]);

            if (!mathematicalConverterServer.isTheDateCorrect(parseRequestParameters[MATH_OPERATION], repositoryValue, requestValue) &&
                    greedy) {
                return false;
            }
            if (mathematicalConverterServer.isTheDateCorrect(parseRequestParameters[MATH_OPERATION], repositoryValue, requestValue) &&
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
                            argumentsConverterServer.getTypedValue(x[COLUMN_NAME], x[COLUMN_VALUE])));
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

        if (data.matches(".*\\blike\\b.*")) {
            return parseLikeAndIlikeRequest(data, "like");
        } else {
            if (data.matches(".*\\bilike\\b.*")) {
                return parseLikeAndIlikeRequest(data, "ilike");
            }
        }

        array[MATH_OPERATION] = data.
                replaceAll("[^!=><]", "");

        array[COLUMN_NAME] = data.
                substring(COLUMN_NAME, data.indexOf(array[MATH_OPERATION])).
                trim().replaceAll("'", "").
                toLowerCase();

        array[COLUMN_VALUE] = data.
                substring(data.indexOf(array[MATH_OPERATION]) + array[MATH_OPERATION].length()).
                trim().
                replaceAll("'", "");
        return array;
    }

    public String[] parseLikeAndIlikeRequest(String request, String operationName) {
        String[] array = new String[COLUMN_NAME + MATH_OPERATION + COLUMN_VALUE];

        var requestArray = request.split("\\b" + operationName + "\\b");

        array[MATH_OPERATION] = operationName;

        array[COLUMN_NAME] = requestArray[COLUMN_NAME].
                replaceAll("'", "").
                trim().
                toLowerCase();

        array[COLUMN_VALUE] = requestArray[COLUMN_VALUE - 1].
                trim().
                replaceAll("'", "");

        return array;
    }

    public JavaSchoolRepository getJavaSchoolRepository() {
        return javaSchoolRepository;
    }
}
