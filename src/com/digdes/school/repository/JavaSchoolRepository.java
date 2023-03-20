package com.digdes.school.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaSchoolRepository {
    private List<Map<String, Object>> repository;

    public JavaSchoolRepository() {
        this.repository = new ArrayList<>();
    }

    public void insert(String key, String value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        repository.add(map);
    }

    public List<Map<String, Object>> update(String request) {
        return repository;
    }

    public List<Map<String, Object>> select(String request) {
        return repository;
    }

    public List<Map<String, Object>> delete(String request) {
        return repository;
    }

    public List<Map<String, Object>> getRepository() {
        return repository;
    }
}
