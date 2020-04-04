package com.taxi.sa.parsing;

import com.taxi.sa.parsing.input.InputMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityMapService {

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private PersistanceService persistanceService;

    public void setValidatorService(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    public void setPersistanceService(PersistanceService persistanceService) {
        this.persistanceService = persistanceService;
    }

    public void insertion(InputMap inputMap) {
        validatorService.validate(inputMap.getWalls(),inputMap.getCheckpoints(),inputMap.getWidth(),inputMap.getHeight());
    }

}
