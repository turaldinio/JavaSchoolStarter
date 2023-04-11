package com.digdes.school.server.interfaces;

import java.util.List;
import java.util.Map;

public interface ReversionPolishNotation {
    String getPostfixRequest(String expression);

    List<Map<String,Object>>calculatePostfixRequest(String postfix);
}
