package com.taxi.sa.parsing.output.user;

import com.taxi.sa.parsing.input.user.InputRequest;

public class UserRequest {

    private String cityId;
    private Coordinate source;
    private Coordinate destination;

    public UserRequest(String cityId, InputRequest inputRequest) {
        this.cityId = cityId;
        this.source = new Coordinate(inputRequest.getSource());
        this.destination = new Coordinate(inputRequest.getDestination());
    }

    public String getCityId() {
        return cityId;
    }

    public Coordinate getSource() {
        return source;
    }

    public Coordinate getDestination() {
        return destination;
    }

}
