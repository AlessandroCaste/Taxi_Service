package com.taxi.fe.Controller;

import parsing.JsonValidator;
import parsing.ReceivedMapsInterface;

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

    @RequestMapping(value = "/maps/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> addMap(@Valid @RequestBody ReceivedMapsInterface receivedMap) {
        //TODO Adding exceptions
        jsonValidator.process(receivedMap);
        return new ResponseEntity<String>("ciao", HttpStatus.OK);
    }

    @RequestMapping(value = "/maps/{city}/taxi_positions/{taxiId}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> insertTaxi(@Valid @RequestBody Coordinates coordinates) {
        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user_requests/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> computeRoute(@Valid @RequestBody CityRoute route) {
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}
