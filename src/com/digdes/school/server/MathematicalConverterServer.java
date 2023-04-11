package com.digdes.school.server;

import com.digdes.school.repository.MathematicalConverterRepository;

public class MathematicalConverterServer {
    private final MathematicalConverterRepository mathematicalConverterRepository;

    public MathematicalConverterServer() {
        mathematicalConverterRepository = new MathematicalConverterRepository();
    }

    public Boolean isTheDateCorrect(String columnName, Object repositoryValue, Object requestValue) {
        return mathematicalConverterRepository.getMathematicalSignsMap().get(columnName).parseOperation(repositoryValue, requestValue);
    }


}
