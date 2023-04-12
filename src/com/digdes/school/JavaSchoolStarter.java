package com.digdes.school;

import com.digdes.school.excption.BadRequest;
import com.digdes.school.server.DAOServer;

import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    private DAOServer javaSchoolServer;

    public JavaSchoolStarter() {
        javaSchoolServer = new DAOServer();
    }

    public List<Map<String, Object>> execute(String request) throws Exception {

        return switch (request.substring(0, 6).toLowerCase()) {
            case "insert" -> javaSchoolServer.insert(request);
            case "update" -> javaSchoolServer.update(request);
            case "select" -> javaSchoolServer.select(request);
            case "delete" -> javaSchoolServer.delete(request);
            default -> throw new BadRequest(String.format("%s  operation is not supported",
                    request.substring(0, ' ')));
        };

    }

    public DAOServer getJavaSchoolServer() {
        return javaSchoolServer;
    }
}
