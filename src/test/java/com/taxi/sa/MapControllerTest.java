package com.taxi.sa;

import com.taxi.sa.controller.MapController;
import com.taxi.sa.parsing.JsonValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MapController.class)
public class MapControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JsonValidator jsonValidator;

    // Testing controller correctly sets up
    @Test
    public void basicTest() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to Taxi Service A"))
                .andReturn();
    }

    // MAPS upload feature testing
    // Testing correct json map submission
    @Test
    public void postMap() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/maps/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new String(Files.readAllBytes(Paths.get("src/test/resources/taxi_map.json")))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("{}"))
                .andReturn();
    }

    // Testing wrong submissions are correctly spotted
    @Test
    public void wrongPostMap() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/maps/")
                .contentType(MediaType.APPLICATION_XML))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("json data required"))
                .andReturn();

        mvc.perform(MockMvcRequestBuilders.post("/maps/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"city\": \"Milan\",\n" +
                        "  \"width\": 13,\n" +
                        "  \"height\": 10,\n}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("malformed submission"))
                .andReturn();
    }

}
