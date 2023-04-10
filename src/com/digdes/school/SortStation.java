package com.digdes.school;

import java.util.*;

public class SortStation {
    private static final String AND = "and";
    private static final String OR = "or";

    public double eval(String expression) {
        //разбиваем строку пробелами
        String[] values = Objects.requireNonNull(expression).split(" ");

        Stack<String> myStack = new Stack<>();
        Stack<Double> stack = new Stack<>();
        StringBuilder stringBuilder = new StringBuilder();

        Stack<Map<String, Object>> suitableMaps = new Stack<>();
        for (String value : values) {
            if (value.isBlank()) {
                //если встретился пробел - пропускаем и идем дальше
                continue;
            }
            if (!value.contains("or") || !value.contains("and")) {
                stringBuilder.append(value);
            }

            if (value.matches("\\d+")) {
                //встретилось число - переносим в стек
                stack.push(Double.parseDouble(value));
            } else if (AND.equals(value)) {
                //Встретился знак сложения.
                //Читаем последнее число
                double val2 = stack.pop();
                //Читаем предпоследнее число
                double val1 = stack.pop();

                //Складываем числа
                double result = val1 + val2;
                //Результат сложения переносим в стек
                stack.push(result);
            } else if (OR.equals(value)) {
                //Встретился знак вычитания.
                //Читаем последнее число
                double val2 = stack.pop();
                //Читаем предпоследнее число
                double val1 = stack.pop();

                //Из предпоследнего числа вычитаем последнее
                double result = val1 - val2;
                //Результат вычитания переносим в стек
                stack.push(result);
            } else {
                throw new IllegalArgumentException("unsupported value: " + value);
            }
        }

        //Читаем результат вычисления из стека
        double result = stack.pop();

        //Если в стеке еще что-то осталось после вычислений - значит исходное выражение было некорректно. Ошибка
        if (!stack.isEmpty()) {
            throw new IllegalArgumentException("expression[" + expression + "] is incorrect");
        }

        return result;
    }
}

