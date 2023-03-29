package com.digdes.school;

import com.digdes.school.excption.BadRequest;
import com.digdes.school.server.JavaSchoolServer;

import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    private JavaSchoolServer javaSchoolServer;

    public JavaSchoolStarter() {
        javaSchoolServer = new JavaSchoolServer();
    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        try {
            request = request.toLowerCase();

            return switch (request.substring(0, 6)) {
                case "insert" -> javaSchoolServer.insert(request);
                case "update" -> javaSchoolServer.update(request);
                case "select" -> javaSchoolServer.select(request);
                case "delete" -> javaSchoolServer.delete(request);
                default -> throw new BadRequest(String.format("%s  operation is not supported",
                        request.substring(0, ' ')));
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
