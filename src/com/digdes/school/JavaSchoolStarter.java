package com.digdes.school;

import com.digdes.school.server.JavaSchoolStarterServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    private JavaSchoolStarterServer javaSchoolStarterServer;

    public JavaSchoolStarter() {
        javaSchoolStarterServer = new JavaSchoolStarterServer;
    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        switch (request) {
            case "INSERT":
                return javaSchoolStarterServer.insert(request);
            case "UPDATE":
                return javaSchoolStarterServer.update(request);
            case "SELECT":
                return javaSchoolStarterServer.select(request);
            case "DELETE":
                return javaSchoolStarterServer.delete(request);
        }

        return new ArrayList<>();
    }
}
