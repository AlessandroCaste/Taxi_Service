package com.taxi.sa.parsing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taxi.sa.parsing.city.Checkpoint;
import com.taxi.sa.parsing.city.CityMap;
import com.taxi.sa.parsing.city.Wall;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;

public class ReceivedMap {

    @JsonProperty("city")
    @NotEmpty
    private String cityId;
    private int width;
    private int height;
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Checkpoint> checkpoints = new ArrayList<>();

    public String getCityId() {
        return cityId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public CityMap getCityMap(){
        return new CityMap(cityId.toLowerCase(), width, height);
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

}
