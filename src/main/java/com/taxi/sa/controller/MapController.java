package com.taxi.sa.controller;

import com.taxi.sa.exceptions.CityMapParsingException;
import com.taxi.sa.exceptions.TaxiValidationException;
import com.taxi.sa.parsing.InputMapInterface;
import com.taxi.sa.parsing.input.user.InputCoordinate;
import com.taxi.sa.parsing.input.user.InputRequest;
import com.taxi.sa.services.CityMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class MapController {

    @Value("${spring.application.name}")
    String appName;

    private CityMapService cityMapService;

    @Autowired
    public void setCityMapService(CityMapService cityMapService) {
        this.cityMapService = cityMapService;
    }

    @GetMapping("/")
    public String homepage(Model model) {
        model.addAttribute("appName", appName);
        return "Welcome to " + model.getAttribute("appName");
    }

    @Async
    @RequestMapping(value = "/maps/", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addMap(@Valid @RequestBody InputMapInterface inputMap) throws CityMapParsingException {
            cityMapService.insertion(inputMap);
            return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

    @Async
    @RequestMapping(path = "/maps/{city}/taxi_positions/{taxiId}", method = RequestMethod.POST, consumes = "application/json",produces = "application/json")
    public ResponseEntity<String> insertTaxi(@PathVariable(value = "city") String city, @PathVariable(value = "taxiId") String taxiId, @Valid @RequestBody InputCoordinate position)
    throws TaxiValidationException {
        cityMapService.insertion(city, taxiId, position);
        return new ResponseEntity<>("{}",HttpStatus.CREATED);
    }

    @Async
    @RequestMapping(value = "/{city}/user_requests/", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> computeRoute(@PathVariable(value = "city") String city, @Valid @RequestBody InputRequest inputRequest)
    throws Exception {
        String response = cityMapService.insertion(city,inputRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
