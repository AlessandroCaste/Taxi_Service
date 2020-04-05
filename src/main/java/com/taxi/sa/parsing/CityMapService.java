package com.taxi.sa.parsing;

import com.taxi.sa.exceptions.CityMapParsingException;
import com.taxi.sa.exceptions.TaxiValidationException;
import com.taxi.sa.exceptions.UserRequestException;
import com.taxi.sa.parsing.input.user.InputCoordinate;
import com.taxi.sa.parsing.input.user.InputRequest;
import com.taxi.sa.parsing.output.user.UserRequest;
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

    public void insertion(String cityId, String taxiId, InputCoordinate inputCoordinate) throws  TaxiValidationException, PersistenceException {
        boolean validationResult = validatorService.validate(cityId, inputCoordinate);
        if(!validationResult)
            throw new TaxiValidationException();
        persistanceService.save(taxiId,cityId,inputCoordinate);
    }

    public void insertion(String cityId, InputRequest inputRequest) throws UserRequestException {
        boolean validationResult = validatorService.validate(cityId,inputRequest);
        if(!validationResult)
            throw new UserRequestException();
        UserRequest userRequest = new UserRequest(cityId,inputRequest);
    }

}
