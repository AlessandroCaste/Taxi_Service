package parsing;

import java.util.ArrayList;
import java.util.List;

public class ReceivedMap implements parsing.ReceivedMapsInterface {

    private String city;
    private int width;
    private int height;
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Checkpoint> checkpoints = new ArrayList<>();

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
