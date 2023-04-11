package com.digdes.school.repository;

import java.util.*;

public class DAORepository {
    private static DAORepository javaSchoolRepository;
    private List<Map<String, Object>> repository;

    private DAORepository() {
        this.repository = new ArrayList<>();
    }

    public static DAORepository getInstance() {
    if(javaSchoolRepository==null){
        javaSchoolRepository=new DAORepository();
    }
    return javaSchoolRepository;
    }

    public List<Map<String, Object>> insert(Map<String, Object> map) {
        repository.add(map);
        return repository;
    }

    public List<Map<String, Object>> update(List<Map<String, Object>> maps) {
        repository.addAll(maps);
        return repository;
    }

    public List<Map<String, Object>> update(Set<Map<String, Object>> maps) {
        repository.addAll(maps);
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

    public void deleteMap(Iterator<Map<String, Object>> iterator) {
        iterator.remove();
    }

    public Iterator<Map<String, Object>> getIterator() {
        return repository.iterator();
    }


}
