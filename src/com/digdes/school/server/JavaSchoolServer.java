package com.digdes.school.server;

import com.digdes.school.excption.NonExistentParameter;
import com.digdes.school.repository.JavaSchoolRepository;

import java.util.*;
import java.util.stream.Collectors;


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

                String operation = cleanParameters.replaceAll("[^!=><%]", "");
                String columnName = cleanParameters.substring(0, cleanParameters.indexOf(operation)).trim();
                String columnValue = cleanParameters.substring(cleanParameters.indexOf(operation) + 1).trim();

                var typedObject = ConverterClass.getConvertionMap().get(columnName).apply(columnValue);


                map.put(columnName, typedObject);

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

        //UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3
//        if (filterCondition.contains("or") && !filterCondition.contains("and")) {
//            var suitableMaps = parseUpdateRequestWithOr(filterCondition, new LinkedHashSet<>());
//            var updateMapValues = updateValuesInRepository(newValues, suitableMaps);
//            return javaSchoolRepository.update(updateMapValues);
//        }

        if (filterCondition.contains("and") && !filterCondition.contains("or")) {
            List<Map<String, Object>> mapsList = new ArrayList<>();

            String[] filterConditionArray = filterCondition.split("and");

            Iterator<Map<String, Object>> iterator = javaSchoolRepository.getIterator();

            while (iterator.hasNext()) {
                Map<String, Object> map = iterator.next();

                if (checkAvailabilityOfAllKeys(filterConditionArray, map) && checkAvailabilityOfAllKeys(newValues, map)) {
                    if (checkValuesInUpdateRequest(filterConditionArray, map)) {
                        var updatedCollection = updateValuesInRepository(newValues, map);
                        javaSchoolRepository.deleteMap(iterator);
                        mapsList.add(updatedCollection);
                    }
                }
            }
            return javaSchoolRepository.update(mapsList);

        }

        return javaSchoolRepository.getRepository();
    }

    public boolean checkValuesInUpdateRequest(String[] filterArray, Map<String, Object> map) {

        for (String currentFilter : filterArray) {

            var processedRequestData = getProcessedRequestData(currentFilter);

            var repositoryValue = ConverterClass.getConvertionMap().get(processedRequestData[COLUMN_NAME]).apply(String.valueOf(map.get(processedRequestData[COLUMN_NAME])));
            var requestValue = ConverterClass.getConvertionMap().get(processedRequestData[COLUMN_NAME]).apply(processedRequestData[COLUMN_VALUE]);

            if (!ConverterClass.getMathematicalSignsMap().get(processedRequestData[MATH_OPERATION]).parseOperation(repositoryValue, requestValue)) {
                return false;


            }
        }
        return true;

    }


    public Set<Map<String, Object>> parseUpdateRequestWithOr(String request, Set<Map<String, Object>> listMap) {
        var requestParamArray = request.split("or");

        if (requestParamArray.length == 1) {
            String filterOperation = request.replaceAll("[^!=><%]", "");
            String filterColumnName = request.substring(0, request.indexOf(filterOperation)).trim();
            String filterColumnValue = request.substring(request.indexOf(filterOperation) + 1).trim();

            Iterator<Map<String, Object>> iterator = javaSchoolRepository.getIterator();

            while (iterator.hasNext()) {
                Map<String, Object> currentMap = iterator.next();
                if (currentMap.containsKey(filterColumnName)) {
                    var repositoryValue = ConverterClass.getConvertionMap().get(filterColumnName).apply(String.valueOf(currentMap.get(filterColumnName)));
                    var requestValue = ConverterClass.getConvertionMap().get(filterColumnName).apply(filterColumnValue);

                    if (ConverterClass.getMathematicalSignsMap().get(filterOperation).parseOperation(repositoryValue, requestValue)) {

                        listMap.add(currentMap);

                        javaSchoolRepository.deleteMap(iterator);
                        return listMap;
                    }
                }
            }
        } else {
            for (int a = 0; a < requestParamArray.length; a++) {
                parseUpdateRequestWithOr(requestParamArray[a].trim(), listMap);
            }
        }

        return listMap;
    }

    public List<Map<String, Object>> select(String request) {
        return javaSchoolRepository.select(request);


    }

    public List<Map<String, Object>> delete(String request) {
        return javaSchoolRepository.delete(request);


    }

    public Map<String, Object> updateValuesInRepository(String[] newValues, Map<String, Object> map) {
        Map<String, Object> updateMap = map;

        for (String newValue : newValues) {
            var parseNewValue = getProcessedRequestData(newValue);
            updateMap.put(parseNewValue[COLUMN_NAME],
                    ConverterClass.getConvertionMap().
                            get(parseNewValue[COLUMN_NAME]).
                            apply(parseNewValue[COLUMN_VALUE]));

        }

        return updateMap;
    }


    public List<String> keysList(String[] pairs) {
        return Arrays.stream(pairs).map(x -> x.split("[!=><%]")).map(x -> x[0].trim()).collect(Collectors.toList());
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
        String[] array = new String[3];
        array[0] = data.replaceAll("[^!=><%]", "");
        array[1] = data.substring(0, data.indexOf(array[0])).trim();
        array[2] = data.substring(data.indexOf(array[0]) + 1).trim();
        return array;
    }
}
