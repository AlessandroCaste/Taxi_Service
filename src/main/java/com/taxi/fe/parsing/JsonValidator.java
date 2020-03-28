package com.taxi.fe.parsing;

import com.taxi.fe.parsing.city.Checkpoint;
import com.taxi.fe.parsing.city.CityMap;
import com.taxi.fe.parsing.city.Wall;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JsonValidator {

    public void process(ReceivedMap receivedMap) {
            CityMap cityMap = receivedMap.getCityMap();
            ArrayList<Wall> walls = receivedMap.getWalls();
            ArrayList<Checkpoint> checkpoints = receivedMap.getCheckpoints();
            for(Wall wall : walls)
                checkIntegrity(wall,cityMap.getWidth(),cityMap.getHeight());
    }

    private void checkIntegrity(Wall wall, int width, int height) {
    }

}

