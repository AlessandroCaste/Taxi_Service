package com.taxi.sa.parsing;

import com.taxi.sa.exceptions.CityMapParsingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;

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

    public void insertion(InputMapInterface inputMap) throws CityMapParsingException, PersistenceException {
        boolean validationResult = validatorService.validate(inputMap);
        if(!validationResult)
            throw new CityMapParsingException();
        persistanceService.save(inputMap);
    }

}
