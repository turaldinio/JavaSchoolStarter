package com.digdes.school.server;


import com.digdes.school.server.interfaces.ReversionPolishNotation;

import java.util.*;

public class DijkstraParser implements ReversionPolishNotation {


    private final DAOServer javaSchoolServer;

    public DijkstraParser(DAOServer javaSchoolServer) {
        this.javaSchoolServer = javaSchoolServer;
    }


    private static Map<String, Integer> priorityMap = Map.of(
            "(", 0
            ,
            "or", 1
            ,
            "and", 2
    );

    @Override
    public String getPostfixRequest(String expression) {
        String[] values = Objects.requireNonNull(expression).split(" ");

        Stack<String> operationStack = new Stack<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (String value : values) {
            if (value.isBlank()) {

                continue;
            }

            if (value.contains("(")) {
                operationStack.push("(");
                continue;
            }

            if (value.contains(")")) {

                for (int a = 0; a < operationStack.size(); a++) {
                    String as = operationStack.peek();
                    if (!as.equals("(")) {
                        stringBuilder.append(operationStack.pop()).append(" ");
                        a = -1;
                    } else {
                        operationStack.pop();
                        break;
                    }
                }
                continue;
            }
            if (!value.contains("or") && !value.contains("and")) {
                stringBuilder.append(value).append(" ");
                continue;
            }


            if (value.contains("and") || value.contains("or")) {
                int currentOperationPriority = priorityMap.get(value);
                for (int a = 0; a < operationStack.size(); a++) {
                    int priority = priorityMap.get(operationStack.peek());
                    if (priority >= currentOperationPriority) {
                        stringBuilder.append(operationStack.pop()).append(" ");
                        a -= 1;
                    }
                }
                operationStack.push(value);
            }
        }

        Collections.reverse(operationStack);
        for (String s : operationStack) {
            stringBuilder.append(s).append(" ");
        }

        return stringBuilder.toString();
    }

    @Override
    public List<Map<String, Object>> calculatePostfixRequest(String postfixLine) {
        Stack<String> stack = new Stack<>();

        List<Map<String, Object>> list = new ArrayList<>();
        Stack<List<Map<String, Object>>> mapStack = new Stack<>();

        var arrayRequest = postfixLine.split(" ");

        for (int a = 0; a < arrayRequest.length; a++) {
            String value = arrayRequest[a];

            if (!value.contains("or") && !value.contains("and")) {
                stack.push(value);
                continue;
            }

            if (value.contains("and")) {
                if (a != arrayRequest.length - 1 && arrayRequest[a + 1].contains("or")) {
                    String[] array = new String[]{stack.pop(), stack.pop()};

                    saveSuitableCollection(mapStack, array, true, javaSchoolServer.getDaoRepository().getRepository());
                    continue;
                }

                if (mapStack.isEmpty()) {
                    String[] array = new String[]{stack.pop(), stack.pop()};

                    saveSuitableCollection(mapStack, array, true, javaSchoolServer.getDaoRepository().getRepository());


                } else {
                    String[] array = new String[]{stack.pop()};

                    var result = findSuitableCollection(array, true, mapStack.pop());
                    if (!result.isEmpty()) {
                        mapStack.push(result);
                    }

                }
                continue;
            }

            if (value.contains("or")) {
                if (stack.isEmpty()) {
                    break;
                }
                if (mapStack.isEmpty()) {
                    String[] array = new String[]{stack.pop(), stack.pop()};

                    saveSuitableCollection(mapStack, array, false, javaSchoolServer.getDaoRepository().getRepository());

                } else {
                    String[] array = new String[]{stack.pop()};

                    var stackList = mapStack.pop();
                    stackList.addAll(findSuitableCollection(array, false, javaSchoolServer.getDaoRepository().getRepository()));
                    mapStack.push(stackList);

                }

            }

        }

        for (List<Map<String, Object>> mapList : mapStack) {
            list.addAll(mapList);
        }

        return list;

    }

    private void saveSuitableCollection(Stack<List<Map<String, Object>>> mapStack, String[] array, boolean greedy, List<Map<String, Object>> map) {
        var result = findSuitableCollection(array, greedy, javaSchoolServer.getDaoRepository().getRepository());
        if (!result.isEmpty()) {
            mapStack.push(result);
        }
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
                }

            }
        }

        return list;
    }

}

