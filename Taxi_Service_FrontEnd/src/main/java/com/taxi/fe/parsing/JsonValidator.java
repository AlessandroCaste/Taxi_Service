package parsing;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JsonValidator {

    public void process(ReceivedMapsInterface receivedMap) {
            CityMap cityMap = receivedMap.getCityMap();
            ArrayList<Wall> walls = receivedMap.getWalls();
            ArrayList<Checkpoint> checkpoints = receivedMap.getCheckpoints();
    }


}

