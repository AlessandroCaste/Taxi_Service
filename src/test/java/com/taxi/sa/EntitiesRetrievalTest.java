package com.taxi.sa;

import com.taxi.sa.parsing.output.city.Checkpoint;
import com.taxi.sa.parsing.output.city.CityMap;
import com.taxi.sa.parsing.output.city.Wall;
import com.taxi.sa.parsing.output.user.Taxi;
import com.taxi.sa.repositories.CheckpointRepository;
import com.taxi.sa.repositories.MapRepository;
import com.taxi.sa.repositories.TaxiRepository;
import com.taxi.sa.repositories.WallRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EntitiesRetrievalTest {

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

    @Autowired
    private TestEntityManager taxiManager;

    @Autowired
    private TaxiRepository taxiRepository;

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
        assertThat(foundCity.isPresent(),is(true));
        assertThat(foundCity.get().toString(),is("City = milan width = 13 height = 10"));

        // Testing Wall retrievals
        Optional<List<Wall>> foundWalls = wallRepository.findAllByCityMap(milan);
        assertThat(foundWalls.isPresent(),is(true));
        ArrayList<Wall> foundWallsList = new ArrayList<>(foundWalls.get());
        assertThat(foundWallsList.size(), is(2));
        Wall wall1 = foundWallsList.get(0);
        Wall wall2 = foundWallsList.get(1);
        assertThat(wall1.coordinatesToString(),is("(3,3)->(4,3)"));
        assertThat(wall2.coordinatesToString(),is("(4,4)->(5,4)"));

        // Testing Checkpoints retrieval
        Optional<List<Checkpoint>> foundCheckpoints = checkpointRepository.findAllByCityMap(milan);
        assertThat(foundCheckpoints.isPresent(),is(true));
        ArrayList<Checkpoint> foundCheckpointsList = new ArrayList<>(foundCheckpoints.get());
        assertThat(foundCheckpointsList.size(), is(2));
        Checkpoint checkpoint1 = foundCheckpointsList.get(0);
        Checkpoint checkpoint2 = foundCheckpointsList.get(1);
        assertThat(checkpoint1.getPrice(), is(1.0f));
        assertThat(checkpoint2.getPrice(), is(3.0f));
        assertThat(checkpoint1.coordinatesToString(), is("(3,3)->(4,3)"));
        assertThat(checkpoint2.coordinatesToString(), is("(4,4)->(4,5)"));
    }

    @Test
    public void taxiRetrieval() {

        // Setupping the data
        CityMap milan = new CityMap("milan",13,10);
        mapManager.persist(milan);
        mapManager.flush();

        // Creating many taxis with the same id to check integrity
        Taxi taxi1 = new Taxi("taxi blu 01",1,1);
        Taxi taxi2 = new Taxi("taxi blu 01",2,2);
        Taxi taxi3 = new Taxi("taxi blu 01",3,3);
        Taxi taxi4 = new Taxi("taxi blu 01",4,4);
        Taxi taxi5 = new Taxi("taxi blu 01",5,5);
        ArrayList<Taxi> taxis = new ArrayList<>(Arrays.asList(taxi1,taxi2,taxi3,taxi4,taxi5));
        for(Taxi taxi : taxis)
            milan.addTaxi(taxi);
        taxiManager.flush();

        // Linking the city to the taxi
        Optional<List<Taxi>> foundTaxis = taxiRepository.findAllByCityMap(milan);
        ArrayList<Taxi> foundTaxisList = new ArrayList<>(foundTaxis.get());

        assertThat(foundTaxisList.size(), is(1));
        assertThat(taxi5,is(foundTaxisList.get(0)));
    }
}

