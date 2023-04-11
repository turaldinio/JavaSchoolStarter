package com.digdes.school.server;

import com.digdes.school.repository.MathematicalConverterRepository;
import com.digdes.school.server.interfaces.ArgumentsTypesConverter;
import com.digdes.school.server.interfaces.MathematicalConverter;

public class MathematicalConverterImpl implements MathematicalConverter {
    private static MathematicalConverterImpl mathematicalConverter;

    private final MathematicalConverterRepository mathematicalConverterRepository;

    private MathematicalConverterImpl() {
        mathematicalConverterRepository = MathematicalConverterRepository.getInstance();
    }

    public static MathematicalConverterImpl getInstance() {
        if (mathematicalConverter == null) {
            mathematicalConverter = new MathematicalConverterImpl();
        }
        return mathematicalConverter;
    }


    @Override
    public Boolean isTheDateCorrect(String mathematicalSymbol, Object repositoryValue, Object requestValue) {
        return mathematicalConverterRepository.getMathematicalSignsMap().get(mathematicalSymbol).parseOperation(repositoryValue, requestValue);

    }
}
