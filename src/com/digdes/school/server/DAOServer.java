package com.digdes.school.server;

import com.digdes.school.excption.InconsistentException;
import com.digdes.school.excption.NonExistentParameter;
import com.digdes.school.repository.DAORepository;
import com.digdes.school.server.interfaces.ArgumentsTypesConverter;
import com.digdes.school.server.interfaces.MathematicalConverter;

import java.util.*;


public class DAOServer {
    private static final int MATH_OPERATION = 1;
    private static final int COLUMN_NAME = 0;
    private static final int COLUMN_VALUE = 2;
    private final DAORepository daoRepository;
    private final DijkstraParser dijkstraParser;
    private final ArgumentsTypesConverter argumentsConverterServer;

    private final MathematicalConverter mathematicalConverterServer;

    public DAOServer() {
        this.daoRepository = DAORepository.getInstance();
        this.dijkstraParser = new DijkstraParser(this);
        this.argumentsConverterServer = ArgumentsTypesConverterImpl.getInstance();
        mathematicalConverterServer = MathematicalConverterImpl.getInstance();
    }

    public List<Map<String, Object>> insert(String request) {
        var map = new HashMap<String, Object>();
        String stub = "insert values";

        var array = request.substring(stub.length()).split(",");

        if (!daoRepository.getRepository().isEmpty()) {
            checkAvailabilityOfAllKeys(getAllArrayKeys(array), true);
        }

        for (String line : array) {

            var processedRequestData = getProcessedRequestData(line);
            var typedObject = argumentsConverterServer.getTypedValue(processedRequestData[COLUMN_NAME], processedRequestData[COLUMN_VALUE]);

            map.put(processedRequestData[COLUMN_NAME], typedObject);

        }
        return daoRepository.insert(map);

    }

    public List<Map<String, Object>> update(String request) {
        String stub = "update values";

        String[] newValues = request.substring(stub.length(), request.indexOf("where")).split(",");

        checkAvailabilityOfAllKeys(getAllArrayKeys(newValues), false);


        var suitableCollection = findASuitableCollection(request);

        return daoRepository.update(updateValuesInRepository(newValues, suitableCollection));

    }

    public List<Map<String, Object>> select(String request) {
        return findASuitableCollection(request);

    }

    public List<Map<String, Object>> delete(String request) {
        return daoRepository.delete(findASuitableCollection(request));
    }

    public List<Map<String, Object>> findASuitableCollection(String request) {
        if (request.trim().contains("where")) {

            String filterCondition = request.
                    substring(request.
                            indexOf("where") + "where".
                            length());

            String[] filterConditionArray = null;
            boolean greedy = false;
            List<Map<String, Object>> result = null;


            if (!filterCondition.contains("and") && !filterCondition.contains("or")) {
                filterConditionArray = parseSingletonRequest(filterCondition);
                greedy = true;

                result = filterTheCollection(filterConditionArray, greedy);

            } else {
                result = dijkstraParser.calculatePostfixRequest(dijkstraParser.getPostfixRequest(filterCondition));
            }

            return result;

        } else {
            return daoRepository.getRepository();
        }
    }

    public String[] parseSingletonRequest(String request) {
        return request.split("\n");
    }


    public boolean checkingValidityOfValues(String[] filterArray, Map<String, Object> map, boolean greedy) {
        for (String currentFilter : filterArray) {

            var parseRequestParameters = getProcessedRequestData(currentFilter);

            var repositoryValue = map.get(parseRequestParameters[COLUMN_NAME]);

            var requestValue = argumentsConverterServer.getTypedValue(parseRequestParameters[COLUMN_NAME], parseRequestParameters[COLUMN_VALUE]);

            if (!mathematicalConverterServer.isTheDateCorrect(parseRequestParameters[MATH_OPERATION], repositoryValue, requestValue) &&
                    greedy) {
                return false;
            }
            if (mathematicalConverterServer.isTheDateCorrect(parseRequestParameters[MATH_OPERATION], repositoryValue, requestValue) &&
                    !greedy) {
                return true;
            }


        }
        return greedy;

    }


    public List<Map<String, Object>> filterTheCollection(String[] requestLine, boolean greedy) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        Iterator<Map<String, Object>> iterator = daoRepository.getIterator();

        while (iterator.hasNext()) {
            Map<String, Object> currentMap = iterator.next();

            if (checkAvailabilityOfAllKeys(requestLine, currentMap)) {

                if (checkingValidityOfValues(requestLine, currentMap, greedy)) {
                    mapList.add(currentMap);
                }

            }
        }

        return mapList;
    }


    public List<Map<String, Object>> updateValuesInRepository(String[] newValues, List<Map<String, Object>> map) {
        List<Map<String, Object>> copyMap = new ArrayList<>(map);
        for (Map<String, Object> pairs : map) {
            Arrays.stream(newValues).
                    map(this::getProcessedRequestData).
                    forEach(x -> pairs.put(x[COLUMN_NAME],
                            argumentsConverterServer.
                                    getTypedValue(x[COLUMN_NAME], x[COLUMN_VALUE])));
        }
        daoRepository.deleteMapInList(copyMap);

        return map;
    }

    public void checkAvailabilityOfAllKeys(List<String> request, boolean availabilityOfAll) {
        for (String key : request) {
            if (!daoRepository.getRepository().get(0).containsKey(key)) {
                throw new InconsistentException("the data is not inconsistent");
            }
        }
        if (availabilityOfAll && request.size() != daoRepository.getRepository().get(0).size()) {
            throw new InconsistentException("the data is not inconsistent");

        }


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
        String[] array = new String[MATH_OPERATION + COLUMN_NAME + COLUMN_VALUE];

        if (data.matches(".*\\blike\\b.*")) {
            return parseLikeAndIlikeRequest(data, "like");
        } else {
            if (data.matches(".*\\bilike\\b.*")) {
                return parseLikeAndIlikeRequest(data, "ilike");
            }
        }

        array[MATH_OPERATION] = data.
                replaceAll("[^!=><]", "");

        array[COLUMN_NAME] = data.
                substring(COLUMN_NAME, data.indexOf(array[MATH_OPERATION])).
                trim().replaceAll("'", "").
                toLowerCase();

        array[COLUMN_VALUE] = data.
                substring(data.indexOf(array[MATH_OPERATION]) + array[MATH_OPERATION].length()).
                trim().
                replaceAll("'", "");
        return array;
    }

    public String[] parseLikeAndIlikeRequest(String request, String operationName) {
        String[] array = new String[COLUMN_NAME + MATH_OPERATION + COLUMN_VALUE];

        var requestArray = request.split("\\b" + operationName + "\\b");

        array[MATH_OPERATION] = operationName;

        array[COLUMN_NAME] = requestArray[COLUMN_NAME].
                replaceAll("'", "").
                trim().
                toLowerCase();

        array[COLUMN_VALUE] = requestArray[COLUMN_VALUE - 1].
                trim().
                replaceAll("'", "");

        return array;
    }

    public List<String> getAllArrayKeys(String[] request) {
        List<String> result = new ArrayList<>();

        for (String data : request) {
            result.add(data.substring(0, data.indexOf("=")).replaceAll("'", "").trim().toLowerCase());
        }
        return result;

    }


    public DAORepository getDaoRepository() {
        return daoRepository;
    }
}
