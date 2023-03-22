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
        var map = new HashMap<String, Object>();

        String stub = "update values";

        var condition = conditionParser(request);


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
