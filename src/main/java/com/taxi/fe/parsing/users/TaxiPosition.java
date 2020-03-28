package com.taxi.fe.parsing.users;

public class TaxiPosition {

    private String taxiId;
    private String city;
    private Coordinate coordinate;

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setTaxiId(String taxiId) {
        this.taxiId = taxiId;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
