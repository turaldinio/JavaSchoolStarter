package com.digdes.school;

import com.digdes.school.server.JavaSchoolServer;

import java.util.*;

public class SortStation {
    private JavaSchoolServer javaSchoolServer;

    public SortStation(JavaSchoolServer javaSchoolServer) {
        this.javaSchoolServer = javaSchoolServer;
    }

    private static Map<String, Integer> operationPriority = Map.of(
            "(", 0
            ,
            "or", 1
            ,
            "and", 2
    );

    public String getPostfixRequest(String expression) {
        //разбиваем строку пробелами
        String[] values = Objects.requireNonNull(expression).split(" ");

        Stack<String> myStack = new Stack<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (String value : values) {
            if (value.isBlank()) {

                continue;
            }

            if (value.contains("(")) {
                myStack.push("(");
                continue;
            }

            if (value.contains(")")) {

                for (int a = 0; a < myStack.size(); a++) {
                    String as = myStack.peek();
                    if (!as.equals("(")) {
                        stringBuilder.append(myStack.pop()).append(" ");
                        a = -1;
                    } else {
                        myStack.pop();
                        break;
                    }
                }
                continue;
            }
            //where 'id' = 3 and 'lastname' like '%ул%' or 'id'=1
            if (!value.contains("or") && !value.contains("and")) {
                stringBuilder.append(value).append(" ");
                continue;
            }


            if (value.contains("and") || value.contains("or")) {
                int currentOperationPriority = operationPriority.get(value);
                Iterator<String> iterator = myStack.iterator();
                while (iterator.hasNext()) {
                    String stackValue = iterator.next();
                    int priority = operationPriority.get(stackValue);
                    if (priority >= currentOperationPriority) {
                        stringBuilder.append(stackValue).append(" ");
                        iterator.remove();
                    }
                }
                myStack.push(value);

            }


        }
        for (String s : myStack) {
            stringBuilder.append(s).append(" ");
        }

        return stringBuilder.toString();
    }

    public List<Map<String, Object>> calculatePostfixRequest(String postfixLine) {
        //'id'>=3 'cost'>0 and 'age'>25 id>1 or and
        Stack<String> stack = new Stack<>();

        List<Map<String, Object>> list = new ArrayList<>();
        Stack<List<Map<String, Object>>> mapStack = new Stack<>();

        var arrayRequest = postfixLine.split(" ");

        for (String value : arrayRequest) {

            if (!value.contains("or") && !value.contains("and")) {
                stack.push(value);
                continue;
            }
            if (value.contains("and")) {
                if (mapStack.isEmpty()) {
                    String[] array = new String[]{stack.pop(), stack.pop()};

                    var result = findSuitableCollection(array, true, javaSchoolServer.getJavaSchoolRepository().getRepository());
                    if (!result.isEmpty()) {
                        mapStack.push(result);
                    }
                } else {
                    String[] array = null;
                    var result = findSuitableCollection(array, true, mapStack.pop());
                    if (!result.isEmpty()) {
                        mapStack.push(result);
                    }

                }
                continue;
            }

            if (value.contains("or")) {
                if (mapStack.isEmpty()) {
                    String[] array = new String[]{stack.pop(), stack.pop()};

                    var result = findSuitableCollection(array, false, javaSchoolServer.getJavaSchoolRepository().getRepository());
                    if (!result.isEmpty()) {
                        mapStack.push(result);
                    }

                } else {
                    String[] array = new String[]{stack.pop()};
                    var stackList = mapStack.pop();
                    stackList.addAll(findSuitableCollection(array, false, javaSchoolServer.getJavaSchoolRepository().getRepository()));
                    mapStack.push(stackList);

                }

            }

        }

        for (List<Map<String, Object>> mapList : mapStack) {
            list.addAll(mapList);
        }


        return list;


    }


    private List<Map<String, Object>> findSuitableCollection(String[] filterParam, boolean greedy, List<
            Map<String, Object>> repository) {
        List<Map<String, Object>> list = new ArrayList<>();
        Iterator<Map<String, Object>> iterator = repository.iterator();
        while (iterator.hasNext()) {
            var currentMap = iterator.next();

            if (javaSchoolServer.checkAvailabilityOfAllKeys(filterParam, currentMap)) {

                if (javaSchoolServer.checkingValidityOfValues(filterParam, currentMap, greedy)) {
                    list.add(currentMap);
                    javaSchoolServer.getJavaSchoolRepository().deleteMap(iterator);
                }

            }
        }


        return list;
    }


    private boolean checkMapInValidityColumnName(Map<String, Object> map) {
        for (Map.Entry<String, Object> pairs : map.entrySet()) {
            if (!checkThePresenceOfTheColumnNameInTheRepository(pairs.getKey())) {
                return false;
            }
        }
        return true;
    }

    // TODO: 11.04.2023 подразумевается что данные консистентны
    private boolean checkThePresenceOfTheColumnNameInTheRepository(String columnName) {
        return javaSchoolServer.getJavaSchoolRepository().getRepository().get(0).containsKey(columnName);
    }


}

