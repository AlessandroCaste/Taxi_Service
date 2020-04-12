package com.taxi.sa;

import com.taxi.sa.parsing.output.city.Checkpoint;
import com.taxi.sa.parsing.output.city.CityMap;
import com.taxi.sa.parsing.output.city.Wall;
import com.taxi.sa.parsing.output.user.Taxi;
import com.taxi.sa.repositories.CheckpointRepository;
import com.taxi.sa.repositories.MapRepository;
import com.taxi.sa.repositories.TaxiRepository;
import com.taxi.sa.repositories.WallRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@ContextConfiguration(classes=Application.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class MapIntegrationTest {

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private WallRepository wallRepository;

    @Autowired
    private CheckpointRepository checkpointRepository;

    @Autowired
    private TaxiRepository taxiRepository;

    @Autowired
    private MockMvc mvc;

    // Loading the default map before starting the tests
    @Before
    public void postAMap() throws Exception {
        mvc.perform(post( "/maps/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new String(Files.readAllBytes(Paths.get("src/test/resources/taxi_map.json")))))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{}"));
    }

    // The milan map has been uploaded: checks ensue
    @Test
    public void validatingMapUpload() throws Exception {
        // Testing Map retrieval
        Optional<CityMap> foundCity = mapRepository.findById("milan");
        assertThat(foundCity.isPresent(),is(true));
        assertThat(foundCity.get().toString(),is("City = milan width = 13 height = 10"));

        // Checking walls have been added
        Optional<List<Wall>> foundWalls = wallRepository.findAllByCityMap(foundCity.get());
        assertThat(foundWalls.isPresent(), is(true));

        ArrayList<Wall> walls = new ArrayList<>(foundWalls.get());
        assertThat(walls.size(),is(27));

        // Sampling a few walls
        assertThat(walls.get(0).coordinatesToString(),is("(4,1)->(4,2)"));
        assertThat(walls.get(10).coordinatesToString(),is("(5,8)->(5,9)"));
        assertThat(walls.get(26).coordinatesToString(),is("(10,8)->(11,8)"));

        // Checking checkpoints insertion
        Optional<List<Checkpoint>> foundCheckpoints = checkpointRepository.findAllByCityMap(foundCity.get());
        assertThat(foundCheckpoints.isPresent(), is(true));

        ArrayList<Checkpoint> checkpoints = new ArrayList<>(foundCheckpoints.get());
        assertThat(checkpoints.size(),is(4));

        // Sampling a couple of checkpoints
        assertThat(checkpoints.get(2).coordinatesToString(),is("(7,1)->(7,2)"));
        assertThat(checkpoints.get(2).getPrice(),is(3f));

        assertThat(checkpoints.get(3).coordinatesToString(),is("(7,8)->(7,9)"));
        assertThat(checkpoints.get(3).getPrice(),is(3f));

    }

    // Name's pretty explicative
    @Test
    public void postingATaxi() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/maps/milan/taxi_positions/taxiblu/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new String(Files.readAllBytes(Paths.get("src/test/resources/taxi_position.json")))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{}"))
                .andReturn();

        // Testing taxi retrieval
        Optional<Taxi> foundTaxi = taxiRepository.findById("taxiblu");
        assertThat(foundTaxi.isPresent(),is(true));
        Taxi taxi = foundTaxi.get();
        assertThat(taxi.getCoordinate().toString(),is("(2,6)"));

        // Adding another taxi with the same id; verifying the previous entry is correctly overwritten
        mvc.perform(MockMvcRequestBuilders.post("/maps/milan/taxi_positions/taxiblu/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"x\":2, \"y\":2}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{}"))
                .andReturn();

        foundTaxi = taxiRepository.findById("taxiblu");
        assertThat(foundTaxi.isPresent(),is(true));
        taxi = foundTaxi.get();
        assertThat(taxi.getCoordinate().toString(),is("(2,2)"));

        // Checking there's only one entry
        Optional<CityMap> foundCity = mapRepository.findById("milan");
        assertThat(foundCity.isPresent(),is(true));
        CityMap milan = foundCity.get();
        Optional<List<Taxi>> foundTaxis = taxiRepository.findAllByCityMap(milan);
        assertThat(foundTaxis.isPresent(),is(true));
        ArrayList<Taxi> milanTaxis = new ArrayList<>(foundTaxis.get());
        assertThat(milanTaxis.size(),is(1));

        // Posting taxi with wrong coordinates
        mvc.perform(MockMvcRequestBuilders.post("/maps/milan/taxi_positions/taxibad/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"x\":15, \"y\":2}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"message\" : \"invalid taxi submission\",\"httpStatus\":400}"))
                .andReturn();
    }

    // Trying loading a huge number of cities and taxis
    @Test
    public void loadTesting() throws Exception {
        Random r = new Random();

        // Many cities: I'm attaching a different city name to the standard milan body
        // This  way I'll get many walls/checkpoints-stuffed cities and no entities reuse
        String cityMapBody = new String(Files.readAllBytes(Paths.get("src/test/resources/city_map_body.json")));
        for(int counter = 0; counter<500; counter++) {
            mvc.perform(post("/maps/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"city\": \"milan" + counter + "\"," + cityMapBody))
                    .andExpect(status().isCreated())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("{}"));
            Optional<CityMap> foundMap = mapRepository.findById("milan"+counter);
            assertThat(foundMap.isPresent(),is(true));
        }
        ArrayList<CityMap> cities = new ArrayList<>(mapRepository.findAll());
        assertThat(cities.size(),is(501));

        // Many taxis
        for(int counter = 0; counter<500; counter++) {
            int x = r.nextInt(12) + 1;
            int y = r.nextInt(9) + 1;
            String ciao = "{ \"x\":"+ x + ",\"y\":" + y + "}";
            mvc.perform(MockMvcRequestBuilders.post("/maps/milan/taxi_positions/taxi " + counter + "/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"x\":" + x + ", \"y\":" + y + "}"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("{}"))
                    .andReturn();
        }
        ArrayList<Taxi> taxis = new ArrayList<>(taxiRepository.findAll());
        assertThat(taxis.size(),is(500));
    }

}
