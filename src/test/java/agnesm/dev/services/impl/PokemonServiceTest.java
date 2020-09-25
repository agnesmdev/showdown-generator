package agnesm.dev.services.impl;

import agnesm.dev.TestUtil;
import agnesm.dev.apis.PokemonApi;
import agnesm.dev.models.GenerationInfo;
import agnesm.dev.models.Pokemon;
import agnesm.dev.models.Type;
import agnesm.dev.services.PokemonService;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PokemonServiceTest extends TestUtil {

    private final PokemonApi pokemonApi = mock(PokemonApi.class);
    private final Logger logger = LoggerFactory.getLogger(PokemonServiceTest.class);
    private final Random random = new Random();
    private final PokemonService pokemonService = new PokemonServiceImpl(pokemonApi, logger, random);

    private final FakeValuesService fakeValuesService = new FakeValuesService(new Locale("fr-FR"), new RandomService());

    @BeforeEach
    private void before() {
        reset(pokemonApi);
    }

    @Test
    public void generateRandomizedTeamSucceedsWithMoves() throws Exception {
        GenerationInfo info = randomGenerationInfo();
        Pokemon pokemon_1 = randomPokemon();
        Pokemon pokemon_2 = randomPokemon();
        Pokemon pokemon_3 = randomPokemon();
        Pokemon pokemon_4 = randomPokemon();
        Pokemon pokemon_5 = randomPokemon();
        Pokemon pokemon_6 = randomPokemon();

        when(pokemonApi.getGenerationInfo(anyInt())).thenReturn(CompletableFuture.completedFuture(info));
        when(pokemonApi.getPokemonById(anyInt())).thenReturn(
                CompletableFuture.completedFuture(pokemon_1),
                CompletableFuture.completedFuture(pokemon_2),
                CompletableFuture.completedFuture(pokemon_3),
                CompletableFuture.completedFuture(pokemon_4),
                CompletableFuture.completedFuture(pokemon_5),
                CompletableFuture.completedFuture(pokemon_6)
        );
        when(pokemonApi.getMovesByType(any(Type.class))).thenReturn(CompletableFuture.completedFuture(randomMoves()));

        CompletableFuture<List<Pokemon>> result = pokemonService.generateRandomizedTeam(info.getId(), true);
        Thread.sleep(200);

        assertThat(result.isDone());
        List<Pokemon> pokemon = result.get();

        assertThat(pokemon.size() == 6);
        assertThat(pokemon.stream().noneMatch(p -> p.getMoves().isEmpty()));

        verify(pokemonApi).getGenerationInfo(info.getId());
        verify(pokemonApi, times(6)).getPokemonById(anyInt());

        List<Type> types = new ArrayList<Type>();
        pokemon.forEach(p -> {
            types.add(p.getPrimaryType());
            if (p.getSecondaryType() != null) types.add(p.getSecondaryType());
        });
        int numberOfTypes = (int) types.stream().filter(Objects::nonNull).count();

        verify(pokemonApi, times(numberOfTypes)).getMovesByType(any(Type.class));
    }

    @Test
    public void generateRandomizedTeamSucceedsWithNoMoves() throws Exception {
        GenerationInfo info = randomGenerationInfo();
        Pokemon pokemon_1 = randomPokemon();
        Pokemon pokemon_2 = randomPokemon();
        Pokemon pokemon_3 = randomPokemon();
        Pokemon pokemon_4 = randomPokemon();
        Pokemon pokemon_5 = randomPokemon();
        Pokemon pokemon_6 = randomPokemon();

        when(pokemonApi.getGenerationInfo(anyInt())).thenReturn(CompletableFuture.completedFuture(info));
        when(pokemonApi.getPokemonById(anyInt())).thenReturn(
                CompletableFuture.completedFuture(pokemon_1),
                CompletableFuture.completedFuture(pokemon_2),
                CompletableFuture.completedFuture(pokemon_3),
                CompletableFuture.completedFuture(pokemon_4),
                CompletableFuture.completedFuture(pokemon_5),
                CompletableFuture.completedFuture(pokemon_6)
        );

        CompletableFuture<List<Pokemon>> result = pokemonService.generateRandomizedTeam(info.getId(), false);
        Thread.sleep(200);

        assertThat(result.isDone());
        List<Pokemon> pokemon = result.get();

        assertThat(pokemon.size() == 6);
        assertThat(pokemon.stream().allMatch(p -> p.getMoves() == null));

        verify(pokemonApi).getGenerationInfo(info.getId());
        verify(pokemonApi, times(6)).getPokemonById(anyInt());
        verify(pokemonApi, never()).getMovesByType(any(Type.class));
    }
}
