package com.taxi.sa.parsing.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taxi.sa.parsing.InputMapInterface;
import com.taxi.sa.parsing.output.CityMap;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;

public class InputMap implements InputMapInterface {

    @JsonProperty("city")
    @NotEmpty
    private String cityId;
    private int width;
    private int height;
    @JsonProperty("walls")
    private ArrayList<InputWall> walls = new ArrayList<>();
    @JsonProperty("checkpoints")
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
