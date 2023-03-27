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

        RequestParam<?> requestParam = null;

        try {
            var array = request.substring(stub.length()).split(",");

            for (String line : array) {
                String cleanParameters = line.replaceAll("'", "").trim();

                String operation = cleanParameters.replaceAll("[^!=><%]", "");
                String columnName = cleanParameters.substring(0, cleanParameters.indexOf(operation)).trim();
                String columnValue = cleanParameters.substring(cleanParameters.indexOf(operation) + 1).trim();


                switch (columnName) {
                    case "id", "age" -> requestParam = new RequestParam<>(Long.parseLong(columnValue));
                    case "cost" -> requestParam = new RequestParam<>(Double.parseDouble(columnValue));
                    case "lastname" -> requestParam = new RequestParam<>(columnValue);
                    case "active" -> {
                        if (columnValue.equals("true") || columnValue.equals("false")) {
                            requestParam = new RequestParam<>(Boolean.getBoolean(columnName));
                        }

                    }
                }

                //     var typedObject = ConverterClass.getConvertionMap().get(columnName).apply(columnValue);

                map.put(columnName, requestParam);

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
                switch (filterColumnName) {
                    case "id", "age" -> {
                        RequestParam<Long> repositoryValue = (RequestParam<Long>) map.get(filterColumnName);
                        RequestParam<Long> requestParam = new RequestParam<>(Long.parseLong(filterColumnValue));

                        if (!ConverterClass.getOperaionMap().get(filterOperation).parseOperation(repositoryValue, requestParam)) {
                            return false;
                        }
                    }
                    case "lastname" -> {
                        RequestParam<String> repositoryValue = (RequestParam<String>) map.get(filterColumnName);
                        RequestParam<String> requestParam = new RequestParam<>(filterColumnValue);

                        if (!ConverterClass.getOperaionMap().get(filterOperation).parseOperation(repositoryValue, requestParam)) {
                            return false;
                        }

                    }
                    case "cost" -> {
                        RequestParam<Double> repositoryValue = (RequestParam<Double>) map.get(filterColumnName);
                        RequestParam<Double> requestParam = new RequestParam<>(Double.valueOf(filterColumnValue));

                        if (!ConverterClass.getOperaionMap().get(filterOperation).parseOperation(repositoryValue, requestParam)) {
                            return false;
                        }
                    }
                    case "active" -> {
                        RequestParam<Boolean> repositoryValue = (RequestParam<Boolean>) map.get(filterColumnName);
                        RequestParam<Boolean> requestParam = new RequestParam<>(Boolean.parseBoolean(filterColumnValue));

                        if (!ConverterClass.getOperaionMap().get(filterOperation).parseOperation(repositoryValue, requestParam)) {
                            return false;
                        }
                    }

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
                    switch (filterColumnName) {
                        case "id", "age" -> {
                            RequestParam<Long> repositoryValue = (RequestParam<Long>) pairs.get(filterColumnName);
                            RequestParam<Long> requestParam = new RequestParam<>(Long.parseLong(filterColumnValue));

                            if (ConverterClass.getOperaionMap().get(filterOperation).parseOperation(repositoryValue, requestParam)) {
                                listMap.add(pairs);
                                return listMap;
                            }
                        }
                        case "lastname" -> {
                            RequestParam<String> repositoryValue = (RequestParam<String>) pairs.get(filterColumnName);
                            RequestParam<String> requestParam = new RequestParam<>(filterColumnValue);

                            if (ConverterClass.getOperaionMap().get(filterOperation).parseOperation(repositoryValue, requestParam)) {
                                listMap.add(pairs);
                                return listMap;
                            }

                        }
                        case "cost" -> {
                            RequestParam<Double> repositoryValue = (RequestParam<Double>) pairs.get(filterColumnName);
                            RequestParam<Double> requestParam = new RequestParam<>(Double.valueOf(filterColumnValue));

                            if (ConverterClass.getOperaionMap().get(filterOperation).parseOperation(repositoryValue, requestParam)) {
                                listMap.add(pairs);
                                return listMap;
                            }
                        }
                        case "active" -> {
                            RequestParam<Boolean> repositoryValue = (RequestParam<Boolean>) pairs.get(filterColumnName);
                            RequestParam<Boolean> requestParam = new RequestParam<>(Boolean.parseBoolean(filterColumnValue));

                            if (ConverterClass.getOperaionMap().get(filterOperation).parseOperation(repositoryValue, requestParam)) {
                                listMap.add(pairs);
                                return listMap;
                            }
                        }

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
