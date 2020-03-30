package com.taxi.sa;

import com.taxi.sa.parsing.city.Checkpoint;
import com.taxi.sa.parsing.city.CityMap;
import com.taxi.sa.parsing.city.Wall;
import com.taxi.sa.repositories.CheckpointRepository;
import com.taxi.sa.repositories.MapRepository;
import com.taxi.sa.repositories.WallRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ControllerMapIntegration {

    @Autowired
    private TestEntityManager mapManager;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private TestEntityManager wallManager;

    @Autowired
    private WallRepository wallRepository;

    @Autowired
    private TestEntityManager checkpointManager;

    @Autowired
    private CheckpointRepository checkpointRepository;

    // Testing basic retrieval behavior, mostly didactic aim
    @Test
    public void checkRetrieval() {

        // New city
        CityMap milan = new CityMap("milan",13,10);
        mapManager.persist(milan);
        mapManager.flush();

        // A couple of its walls
        ArrayList<Wall> walls = new ArrayList<>();
        walls.add(new Wall(3,3,4,3));
        walls.add(new Wall(4,4,5,4));
        for(Wall wall : walls) {
            wall.setCityMap(milan);
            milan.addWall(wall);
            wallManager.persist(wall);
        }
        wallManager.flush();

        // A couple of its checkpoints
        ArrayList<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(new Checkpoint(1,3,3,4,3));
        checkpoints.add(new Checkpoint(3,4,4,4,5));
        for(Checkpoint checkpoint : checkpoints) {
            checkpoint.setCityMap(milan);
            milan.addCheckpoint(checkpoint);
            checkpointManager.persist(checkpoint);
        }
        checkpointManager.flush();

        // Testing Map retrieval
        Optional<CityMap> foundCity = mapRepository.findById(milan.getCityId());
        assertThat(foundCity.isPresent());
        assertThat(foundCity.get().toString().equals("City = milan width = 13 height = 10"));

        // Testing Wall retrievals
        Optional<List<Wall>> foundWalls = wallRepository.findAllByCityMap(milan);
        assertThat(foundWalls.isPresent());
        ArrayList<Wall> foundWallsList = new ArrayList<>(foundWalls.get());
        assertThat(foundWallsList.size() == 2);
        Wall wall1 = foundWallsList.get(0);
        Wall wall2 = foundWallsList.get(1);
        assertThat(wall1.coordinatesToString().equals("(3,3)->(4,3)"));
        assertThat(wall2.coordinatesToString().equals("(4,4)->(5,4)"));

        // Testing Checkpoints retrieval
        Optional<List<Checkpoint>> foundCheckpoints = checkpointRepository.findAllByCityMap(milan);
        assertThat(foundCheckpoints.isPresent());
        ArrayList<Checkpoint> foundCheckpointsList = new ArrayList<>(foundCheckpoints.get());
        assertThat(foundCheckpointsList.size() == 2);
        Checkpoint checkpoint1 = foundCheckpointsList.get(0);
        Checkpoint checkpoint2 = foundCheckpointsList.get(1);
        assertThat(checkpoint1.getPrice() == 1);
        assertThat(checkpoint2.getPrice() == 3);
        assertThat(checkpoint1.coordinatesToString().equals("(3,3)->(4,3)"));
        assertThat(checkpoint2.coordinatesToString().equals("(4,4)->(5,4)"));
    }
}

