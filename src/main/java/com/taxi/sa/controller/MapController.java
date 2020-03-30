package com.taxi.sa.controller;

import com.taxi.sa.parsing.Coordinate;
import com.taxi.sa.parsing.JsonValidator;
import com.taxi.sa.parsing.ReceivedMap;
import com.taxi.sa.parsing.users.Taxi;
import com.taxi.sa.parsing.users.UserRequest;
import org.hibernate.MappingException;
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
        jsonValidator.storeCityMap(receivedMap);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @Async
    @RequestMapping(path = "/maps/{city}/taxi_positions/{taxiId}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> insertTaxi(@PathVariable(value = "city") String city, @PathVariable(value = "taxiId") String taxiId, @Valid @RequestBody Coordinate position) {
        try {
            jsonValidator.storeTaxi(taxiId,city,position);
            return new ResponseEntity<>("{}",HttpStatus.CREATED);
        } catch(MappingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Async
    @RequestMapping(value = "/user_requests/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> computeRoute(@Valid @RequestBody UserRequest userRequest) {
        System.out.println("ciao");
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}
