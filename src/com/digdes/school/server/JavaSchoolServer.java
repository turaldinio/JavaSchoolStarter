package com.digdes.school.server;

import com.digdes.school.RequestParam;
import com.digdes.school.repository.JavaSchoolRepository;

import java.util.*;


public class JavaSchoolServer {
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

        if (request.contains("(")) {
            var result = update(request.substring(request.indexOf("("), request.indexOf(")")));
        }

        String filterCondition = request.
                substring(request.
                        indexOf("where") + "where".
                        length());

        //UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3
        if (request.contains("or") && !request.contains("and")) {
            parseUpdateRequestWithOr(filterCondition, new LinkedHashSet<>());
            // TODO: 27.03.2023 отправляем данные мапы на изменение ее значений

        }
        if (request.contains("and") && !request.contains("or")) {
            for (Map<String, Object> pairs : javaSchoolRepository.getRepository()) {
                if (parseUpdateRequestWithAnd(filterCondition, pairs)) {
                    // TODO: 27.03.2023 отправляем данную мапу на изменение ее значений
                }
            }


        }
        return null;
    }

    public boolean parseUpdateRequestWithAnd(String request, Map<String, Object> map) {
        var requestParamArray = request.split("and");

        for (String line : requestParamArray) {

            String filterOperation = line.replaceAll("[^!=><%]", "");
            String filterColumnName = line.substring(0, line.indexOf(filterOperation)).trim();
            String filterColumnValue = line.substring(line.indexOf(filterOperation) + 1).trim();

            if (map.containsKey(filterColumnName)) {
                var repositoryValue = ConverterClass.getConvertionMap().get(filterColumnName).apply(filterColumnValue);
                var requestValue = ConverterClass.getConvertionMap().get(filterColumnName).apply(filterColumnValue);

                if (!ConverterClass.getTimeMap().get(filterOperation).parseOperation(repositoryValue, requestValue)) {
                    return false;
                }

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

            for (Map<String, Object> pairs : javaSchoolRepository.getRepository()) {
                if (pairs.containsKey(filterColumnName)) {
                    var repositoryValue = ConverterClass.getConvertionMap().get(filterColumnName).apply(filterColumnValue);
                    var requestValue = ConverterClass.getConvertionMap().get(filterColumnName).apply(filterColumnValue);

                    if (ConverterClass.getTimeMap().get(filterOperation).parseOperation(repositoryValue, requestValue)) {
                        listMap.add(pairs);
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


}
