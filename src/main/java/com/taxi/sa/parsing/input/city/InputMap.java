package com.taxi.sa.parsing.input.city;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taxi.sa.parsing.InputMapInterface;
import com.taxi.sa.parsing.output.city.CityMap;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;

public class InputMap implements InputMapInterface {

    @JsonProperty("city")
    @NotEmpty
    private String cityId;
    private int width;
    private int height;

    @JsonProperty("walls")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private ArrayList<InputWall> walls = new ArrayList<>();

    @JsonProperty("checkpoints")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private ArrayList<InputCheckpoint> checkpoints = new ArrayList<>();

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

    public ArrayList<InputWall> getWalls() {
        return walls;
    }

    public ArrayList<InputCheckpoint> getCheckpoints() {
        return checkpoints;
    }

}
