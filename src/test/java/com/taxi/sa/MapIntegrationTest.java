package com.taxi.sa;

import com.taxi.sa.parsing.output.city.Checkpoint;
import com.taxi.sa.parsing.output.city.CityMap;
import com.taxi.sa.parsing.output.city.Wall;
import com.taxi.sa.repositories.CheckpointRepository;
import com.taxi.sa.repositories.MapRepository;
import com.taxi.sa.repositories.TaxiRepository;
import com.taxi.sa.repositories.WallRepository;
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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // The milan man is uploaded: checks ensue
    @Test
    public void postingAMap() throws Exception {
        mvc.perform(post( "/maps/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new String(Files.readAllBytes(Paths.get("src/test/resources/taxi_map.json")))))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("{}"));

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


}
