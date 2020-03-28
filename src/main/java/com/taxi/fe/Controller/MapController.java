package com.taxi.fe.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.taxi.fe.parsing.JsonValidator;
import com.taxi.fe.parsing.ReceivedMap;
import com.taxi.fe.parsing.users.TaxiPosition;
import com.taxi.fe.parsing.users.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;


@RestController
public class MapController {

    @Value("${spring.application.name}")
    String appName;

    private JsonValidator jsonValidator;

    @Autowired
    public void setJsonValidator(JsonValidator jsonValidator) {
        this.jsonValidator = jsonValidator;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "Welcome to " + model.getAttribute("appName");
    }

    @Async
    @RequestMapping(value = "/maps/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> addMap(@Valid @RequestBody ReceivedMap receivedMap) {
        jsonValidator.process(receivedMap);
        return new ResponseEntity<>("ciao", HttpStatus.OK);
    }

    @Async
    @RequestMapping(path = "/maps/{city}/taxi_positions/{taxiId}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> insertTaxi(@Valid @RequestBody TaxiPosition taxiPosition, @PathVariable(value = "city") String city, @PathVariable(value = "taxiId") String taxiId) {
        taxiPosition.setCity(city);
        taxiPosition.setCity(taxiId);
        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

    @Async
    @RequestMapping(value = "/user_requests/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> computeRoute(@Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}
