package com.taxi.sa.parsing;

import com.taxi.sa.parsing.input.city.InputCheckpoint;
import com.taxi.sa.parsing.input.city.InputWall;
import com.taxi.sa.parsing.input.user.InputCoordinate;
import com.taxi.sa.parsing.input.user.InputRequest;
import com.taxi.sa.parsing.output.city.CityMap;
import com.taxi.sa.repositories.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ValidatorService {

    @Autowired
    MapRepository mapRepository;

    public void setMapRepository(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    // A simple check to verify whether walls and checkpoints have been defined within map width and heights
    public boolean validate(InputMapInterface inputMap) {
        int width = inputMap.getWidth();
        int height = inputMap.getHeight();
        ArrayList<InputWall> walls = inputMap.getWalls();
        ArrayList<InputCheckpoint> checkpoints = inputMap.getCheckpoints();
        boolean checkWalls = walls.stream().allMatch(x -> checkCoordinate(x.getX1(),x.getY1(),width,height) && checkCoordinate(x.getX2(),x.getY2(),width,height));
        boolean checkCheckpoints = checkpoints.stream().allMatch(x -> checkCoordinate(x.getX1(),x.getY1(),width,height) && checkCoordinate(x.getX2(),x.getY2(),width,height));
        return checkWalls && checkCheckpoints;
    }

    // The same check is applied to Taxi coordinates
    public boolean validate(String cityId, InputCoordinate inputCoordinate) {
        if(!mapRepository.findById(cityId).isPresent())
            return false;
        int width = retrieveMapWidth(cityId);
        int height = retrieveMapHeight(cityId);
        return checkCoordinate(inputCoordinate.getX(),inputCoordinate.getY(),width,height);
    }

    // Verifying the user requests
    public boolean validate(String cityId, InputRequest inputRequest) {
        if(!mapRepository.findById(cityId).isPresent())
            return false;
        int width = retrieveMapWidth(cityId);
        int height = retrieveMapHeight(cityId);
        InputCoordinate source = inputRequest.getSource();
        InputCoordinate destination = inputRequest.getDestination();
        boolean checkSource = checkCoordinate(source.getX(),source.getY(),width,height);
        boolean checkDestination = checkCoordinate(destination.getX(),destination.getY(),width,height);
        return checkSource && checkDestination;
    }

    // Checking width and height for a coordinate
    private boolean checkCoordinate(int x, int y, int mapWidth, int mapHeight) {
        boolean xCheck = x >= 1 && x <= mapWidth;
        boolean yCheck = y >= 1 && y <= mapHeight;
        return xCheck && yCheck;
    }

    // If the linked map already exists these method retrieves its characteristics from the repository
    private int retrieveMapWidth(String cityId) {
        CityMap referenceMap = mapRepository.findById(cityId).get();
        return referenceMap.getWidth();
    }

    private int retrieveMapHeight(String cityId) {
        CityMap referenceMap = mapRepository.findById(cityId).get();
        return referenceMap.getHeight();
    }

}
