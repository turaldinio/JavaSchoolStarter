package com.digdes.school.server;

import com.digdes.school.repository.JavaSchoolRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class JavaSchoolServer {
    private JavaSchoolRepository javaSchoolRepository;

    public JavaSchoolServer() {
        this.javaSchoolRepository = new JavaSchoolRepository();
    }

    public List<Map<String, Object>> insert(String request) {
        String stub = "insert values";

        try {
            var array = request.substring(stub.length()).split(",");

            for (String line : array) {
                Stream.of(line.replaceAll("'", "").
                                trim()).
                        map(x -> x.
                                split("=")).
                        forEach((values) ->
                                javaSchoolRepository.insert(values[0], values[1]));

            }

        } catch (Exception e) {

        }
        return null;
    }

    public List<Map<String, Object>> update(String request) {
        return javaSchoolRepository.update(request);


    }

    public List<Map<String, Object>> select(String request) {
        return javaSchoolRepository.select(request);


    }

    public List<Map<String, Object>> delete(String request) {
        return javaSchoolRepository.delete(request);


    }

}
