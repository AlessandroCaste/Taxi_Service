package com.taxi.sa.parsing;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.taxi.sa.parsing.input.InputCheckpoint;
import com.taxi.sa.parsing.input.InputMap;
import com.taxi.sa.parsing.input.InputWall;

import java.util.ArrayList;

@JsonDeserialize(as = InputMap.class)
public interface InputMapInterface {

    String getCityId();
    int getWidth();
    int getHeight();
    ArrayList<InputWall> getWalls();
    ArrayList<InputCheckpoint> getCheckpoints();

}
