package agnesm.dev.controllers;

import agnesm.dev.TestUtil;
import agnesm.dev.exceptions.ApiException;
import agnesm.dev.exceptions.JsonException;
import agnesm.dev.models.Pokemon;
import agnesm.dev.services.PokemonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PokemonControllerTest extends TestUtil {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Logger logger;

    @Mock
    private PokemonService service;

    @InjectMocks
    private PokemonController pokemonController;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(pokemonController)
                .setControllerAdvice(new ApiAdvice(logger), new JsonAdvice(logger))
                .build();
    }

    @Test
    public void randomizedSucceedsWithMoves() throws Exception {
        List<Pokemon> pokemon = randomPokemonTeam(true);
        when(service.generateRandomizedTeam(anyInt(), anyBoolean())).thenReturn(CompletableFuture.completedFuture(pokemon));

        MvcResult mvcResult = mockMvc.perform(get("/randomizer?gen=1&moves=true"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/plain"))
                .andExpect(content().string(pokemon.stream().map(Pokemon::toShowdownFormat).collect(Collectors.joining("\n"))));

        verify(service).generateRandomizedTeam(1, true);
    }

    @Test
    public void randomizedSucceedsWithoutMoves() throws Exception {
        List<Pokemon> pokemon = randomPokemonTeam(false);
        when(service.generateRandomizedTeam(anyInt(), anyBoolean())).thenReturn(CompletableFuture.completedFuture(pokemon));

        MvcResult mvcResult = mockMvc.perform(get("/randomizer?gen=2&moves=false"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/plain"))
                .andExpect(content().string(pokemon.stream().map(Pokemon::toShowdownFormat).collect(Collectors.joining("\n"))));

        verify(service).generateRandomizedTeam(2, false);
    }

    @Test
    public void randomizedFailsWithJsonException() throws Exception {
        CompletableFuture<List<Pokemon>> result = new CompletableFuture<>();
        result.completeExceptionally(new JsonException("boom"));
        when(service.generateRandomizedTeam(anyInt(), anyBoolean())).thenReturn(result);

        MvcResult mvcResult = mockMvc.perform(get("/randomizer?gen=3&moves=true"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());

        verify(service).generateRandomizedTeam(3, true);
    }

    @Test
    public void randomizedFailsWithApiException() throws Exception {
        CompletableFuture<List<Pokemon>> result = new CompletableFuture<>();
        result.completeExceptionally(new ApiException(500, "boom"));
        when(service.generateRandomizedTeam(anyInt(), anyBoolean())).thenReturn(result);

        MvcResult mvcResult = mockMvc.perform(get("/randomizer?gen=4&moves=false"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadGateway());

        verify(service).generateRandomizedTeam(4, false);
    }

    @Test
    public void randomizedFailsWithoutGeneration() throws Exception {
        this.mockMvc
                .perform(get("/randomizer?moves=false"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test
    public void randomizedFailsWithoutMoves() throws Exception {
        this.mockMvc
                .perform(get("/randomizer?gen=2"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }
}
