package com.digdes.school;

import com.digdes.school.server.JavaSchoolServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    private JavaSchoolServer javaSchoolServer;

    public JavaSchoolStarter() {
        javaSchoolServer = new JavaSchoolServer();
    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        switch (request) {
            case "INSERT":
                return javaSchoolServer.insert(request);
            case "UPDATE":
                return javaSchoolServer.update(request);
            case "SELECT":
                return javaSchoolServer.select(request);
            case "DELETE":
                return javaSchoolServer.delete(request);
        }

        return new ArrayList<>();
    }
}
