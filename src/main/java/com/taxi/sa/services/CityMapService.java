package com.taxi.sa.services;

import com.taxi.sa.exceptions.CityMapParsingException;
import com.taxi.sa.exceptions.TaxiValidationException;
import com.taxi.sa.exceptions.UserRequestException;
import com.taxi.sa.parsing.InputMapInterface;
import com.taxi.sa.parsing.input.user.InputCoordinate;
import com.taxi.sa.parsing.input.user.InputRequest;
import com.taxi.sa.parsing.output.user.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;

@Service
public class CityMapService {

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private PersistanceService persistanceService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CityMapService.class.getName());

    public void setValidatorService(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    public void setPersistanceService(PersistanceService persistanceService) {
        this.persistanceService = persistanceService;
    }

    public void insertion(InputMapInterface inputMap) throws CityMapParsingException, PersistenceException {
        boolean validationResult = validatorService.validate(inputMap);
        if(!validationResult) {
            LOGGER.error("Invalid map has been submitted");
            throw new CityMapParsingException();
        }
        persistanceService.save(inputMap);
    }

    public void insertion(String cityId, String taxiId, InputCoordinate inputCoordinate) throws  TaxiValidationException, PersistenceException {
        boolean validationResult = validatorService.validate(cityId, inputCoordinate);
        if(!validationResult) {
            LOGGER.error("Taxi position is invalid");
            throw new TaxiValidationException();
        }
        persistanceService.save(taxiId,cityId,inputCoordinate);
    }

    public String insertion(String cityId, InputRequest inputRequest) throws UserRequestException {
        boolean validationResult = validatorService.validate(cityId,inputRequest);
        if(!validationResult) {
            LOGGER.error("User request coordinates are invalid");
            throw new UserRequestException();
        }
        UserRequest userRequest = new UserRequest(cityId,inputRequest);
        return new RequestService().request(cityId,userRequest);
    }

}
