package com.digdes.school.server;

import com.digdes.school.repository.JavaSchoolRepository;

import java.util.List;
import java.util.Map;

public class JavaSchoolServer {
    private JavaSchoolRepository repository;

    public JavaSchoolServer() {
        this.repository = new JavaSchoolRepository();
    }

    public List<Map<String, Object>> insert(String request) {
        return repository.insert(request);
    }

    public List<Map<String, Object>> update(String request) {
        return repository.update(request);


    }

    public List<Map<String, Object>> select(String request) {
        return repository.select(request);


    }

    public List<Map<String, Object>> delete(String request) {
        return repository.delete(request);


    }

}
