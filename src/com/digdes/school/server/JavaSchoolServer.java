package com.digdes.school.server;

import com.digdes.school.repository.JavaSchoolRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        request = request.replaceAll("'", "");
        var map = new HashMap<String, Object>();

        String stub = "update values";

        var arrayNewValues = request.
                substring(stub.length(), request.indexOf("where")).
                trim().
                split(",");

        var oldValues = request.
                substring(request.indexOf("where")
                        + "where".length()).
                trim();

        String operation = oldValues.replaceAll("[^!=><%]", "");
        String columnName = oldValues.substring(0, oldValues.indexOf(operation)).trim();
        String columnValue = oldValues.substring(oldValues.indexOf(operation) + 1).trim();

        for (Map<String, Object> pairs : javaSchoolRepository.getRepository()) {
            if (pairs.containsKey(columnName)) {
                var currentObject = pairs.get(columnName);

                if (currentObject instanceof Long) {
                    Long typedObject = (Long) currentObject;

                }



            }
        }

        return javaSchoolRepository.update(request);


    }

    private Map<String, Object> conditionParser(String request) {
        return null;
    }

    public List<Map<String, Object>> select(String request) {
        return javaSchoolRepository.select(request);


    }

    public List<Map<String, Object>> delete(String request) {
        return javaSchoolRepository.delete(request);


    }


}
