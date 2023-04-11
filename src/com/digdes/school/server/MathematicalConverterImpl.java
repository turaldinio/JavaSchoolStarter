package com.digdes.school.server;

import com.digdes.school.repository.MathematicalConverterRepository;
import com.digdes.school.server.interfaces.MathematicalConverter;

public class MathematicalConverterImpl implements MathematicalConverter {
    private ArgumentsTypesConverterImpl argumentsTypesConverterServer;
    private final MathematicalConverterRepository mathematicalConverterRepository;

    public MathematicalConverterImpl() {
        mathematicalConverterRepository = new MathematicalConverterRepository();
        argumentsTypesConverterServer = new ArgumentsTypesConverterImpl();
    }


    @Override
    public Boolean isTheDateCorrect(String mathematicalSymbol, Object repositoryValue, Object requestValue) {
        return mathematicalConverterRepository.getMathematicalSignsMap().get(mathematicalSymbol).parseOperation(repositoryValue, requestValue);

    }
}
