package com.taxi.fe.parsing;

import com.taxi.fe.parsing.city.Checkpoint;
import com.taxi.fe.parsing.city.CityMap;
import com.taxi.fe.parsing.city.Wall;

import java.util.ArrayList;

public class ReceivedMap {

    private String city;
    private int width;
    private int height;
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Checkpoint> checkpoints = new ArrayList<>();

    public String getCity() {
        return city;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public CityMap getCityMap(){
        return new CityMap(city.toLowerCase(), width, height);
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }


    public ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

}
