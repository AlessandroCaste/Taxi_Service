package com.taxi.fe.parsing.city;

import java.io.Serializable;

public class CityMap implements Serializable {

    private String city;
    private int width;
    private int height;

    public CityMap(String city, int width, int height) {
        this.city = city;
        this.width = width;
        this.height = height;
    }

    public String getCityId() {
        return city;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "City = " + city + " width " + width + " height " + height;
    }

}
