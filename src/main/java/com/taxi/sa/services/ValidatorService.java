package com.taxi.sa.services;

import com.taxi.sa.parsing.InputMapInterface;
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
        boolean checkWalls = walls.stream().allMatch(x -> checkManhattanDistance(x.getX1(),x.getY1(),x.getX2(),x.getY2())
                                                          && checkBoundaries(x.getX1(),x.getY1(),width,height)
                                                          && checkBoundaries(x.getX2(),x.getY2(),width,height));
        boolean checkCheckpoints = checkpoints.stream().allMatch(x -> checkManhattanDistance(x.getX1(),x.getY1(),x.getX2(),x.getY2())
                                                          && checkBoundaries(x.getX1(),x.getY1(),width,height)
                                                          && checkBoundaries(x.getX2(),x.getY2(),width,height));
        return  checkWalls && checkCheckpoints;
    }

    // The same check is applied to Taxi coordinates
    public boolean validate(String cityId, InputCoordinate inputCoordinate) {
        if(!mapRepository.findById(cityId).isPresent())
            return false;
        int width = retrieveMapWidth(cityId);
        int height = retrieveMapHeight(cityId);
        return checkBoundaries(inputCoordinate.getX(),inputCoordinate.getY(),width,height);
    }

    // Verifying the user requests
    public boolean validate(String cityId, InputRequest inputRequest) {
        if(!mapRepository.findById(cityId).isPresent())
            return false;
        int width = retrieveMapWidth(cityId);
        int height = retrieveMapHeight(cityId);
        InputCoordinate source = inputRequest.getSource();
        InputCoordinate destination = inputRequest.getDestination();
        boolean checkSource = checkBoundaries(source.getX(),source.getY(),width,height);
        boolean checkDestination = checkBoundaries(destination.getX(),destination.getY(),width,height);
        boolean sourceDestinationEqual = source.equals(destination);
        return checkSource && checkDestination && !sourceDestinationEqual;
    }

    // Checking width and height for a coordinate
    private boolean checkBoundaries(int x, int y, int mapWidth, int mapHeight) {
        boolean xCheck = x >= 1 && x <= mapWidth;
        boolean yCheck = y >= 1 && y <= mapHeight;
        return xCheck && yCheck;
    }

    private boolean checkManhattanDistance(int x1,int y1,int x2, int y2) {
        int distance = Math.abs(x1 - x2) + Math.abs(y1 - y2);
        return distance == 1;
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
