package com.digdes.school.repository;

import java.util.*;

public class DAORepository {
    private static DAORepository DAORepository;
    private List<Map<String, Object>> repository;

    private DAORepository() {
        this.repository = new ArrayList<>();
    }

    public static DAORepository getInstance() {
        if (DAORepository == null) {
            DAORepository = new DAORepository();
        }
        return DAORepository;
    }

    public List<Map<String, Object>> insert(Map<String, Object> map) {
        repository.add(map);
        return repository;
    }

    public List<Map<String, Object>> update(List<Map<String, Object>> maps) {
        repository.addAll(maps);
        return repository;
    }

    public List<Map<String, Object>> select() {
        return repository;
    }


    public List<Map<String, Object>> getRepository() {
        return repository;
    }

    public void deleteMapInList(List<Map<String, Object>> list) {
        repository.removeAll(list);
    }

    public Iterator<Map<String, Object>> getIterator() {
        return repository.iterator();
    }


    public void delete(List<Map<String, Object>> suitableCollection) {

        repository.removeAll(suitableCollection);
    }
}
