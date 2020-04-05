package com.taxi.sa.parsing;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.taxi.sa.parsing.input.city.InputCheckpoint;
import com.taxi.sa.parsing.input.city.InputMap;
import com.taxi.sa.parsing.input.city.InputWall;

import java.util.ArrayList;

@JsonDeserialize(as = InputMap.class)
public interface InputMapInterface {

    String getCityId();
    int getWidth();
    int getHeight();
    ArrayList<InputWall> getWalls();
    ArrayList<InputCheckpoint> getCheckpoints();

}
