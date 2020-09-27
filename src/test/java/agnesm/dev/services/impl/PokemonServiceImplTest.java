package agnesm.dev.services.impl;

import agnesm.dev.TestUtil;
import agnesm.dev.apis.PokemonApi;
import agnesm.dev.models.GenerationInfo;
import agnesm.dev.models.Pokemon;
import agnesm.dev.models.Type;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceImplTest extends TestUtil {

    @Mock
    private PokemonApi pokemonApi;

    /**
     * No Spies : @Spy private Random random = new Random();
     * WARNING: An illegal reflective access operation has occurred
     * WARNING: Illegal reflective access by org.mockito.internal.util.reflection.AccessibilityChanger (file:/C:/Users/Nicolas%20F%c3%a9dou/.m2/repository/org/mockito/mockito-core/3.3.3/mockito-core-3.3.3.jar) to field java.util.Random.seed
     * WARNING: Please consider reporting this to the maintainers of org.mockito.internal.util.reflection.AccessibilityChanger
     * WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
     * WARNING: All illegal access operations will be denied in a future release
     * But Mocks returns smart nulls when they aren't configured (0, false, empty lists, ...)
     * ^ is it what you want ?
     */
    @Mock
    private Random random;

    @InjectMocks
    private PokemonServiceImpl pokemonService;

    private final FakeValuesService fakeValuesService = new FakeValuesService(new Locale("fr-FR"), new RandomService());

    @Test
    // async is about being fast, so have a long timeout for failures only and repeatedly and quickly check for success
    @Timeout(value = 200)
    public void should_generate_random_team_with_moves() throws Exception {
        int infoID = givenRandomInfoProvidingID();
        int pokemonCount = 6; // why 6 here, where does the production code uses this number or decide to generate 6 pokemons ???
        // if 6 is a production code decision, you may remove following argument and use thenAnswer instead of thenReturn as in #givenPokemonApiProvidesRandomMoves
        givenRandomPokemons(pokemonCount);

        givenPokemonApiProvidesRandomMoves();

        CompletableFuture<List<Pokemon>> result = pokemonService.generateRandomizedTeam(infoID, true);
        while (!result.isDone()) {
            Thread.sleep(2);
        }
        List<Pokemon> pokemon = result.get();
        Assertions.assertAll( // assertAll generates one error messages from all potential failures, so on a failure you don't have a tree hiding the forest, you get the whole picture at once.
                () -> assertThat(result.isDone()),
                () -> assertThat(pokemon.size()).isEqualTo(pokemonCount),
                () -> assertThat(pokemon.stream()).noneMatch(p -> p.getMoves().isEmpty()),
                () -> verify(pokemonApi).getGenerationInfo(infoID),
                () -> verify(pokemonApi, times(pokemonCount)).getPokemonById(anyInt()),
                () -> {
                    // HashSet avoids doubles in collection, if not a simple counter is enough ?
                    List<Type> types = new ArrayList<Type>();
                    pokemon.forEach(p -> {
                        if (p.getPrimaryType() != null) types.add(p.getPrimaryType());
                        if (p.getSecondaryType() != null) types.add(p.getSecondaryType());
                    });
                    verify(pokemonApi, times(types.size())).getMovesByType(any(Type.class));
                }
        );
    }

    private void givenPokemonApiProvidesRandomMoves() {
        // answer is a lambda invoked at each call
        when(pokemonApi.getMovesByType(any(Type.class))).thenAnswer((T) -> CompletableFuture.completedFuture(randomMoves()));
    }

    private void givenRandomPokemons(int pokemonCount) {
        // one may love streams and wonder how to generate a loop, but a for loop is much more direct/lighter for computers
//        IntStream.range(0, 6).forEach(uselessParam ->
//                when(pokemonApi.getPokemonById(anyInt())).thenReturn(CompletableFuture.completedFuture(randomPokemon()))
//        );
        for (int i = 0; i < pokemonCount; i++) {
            when(pokemonApi.getPokemonById(anyInt())).thenReturn(CompletableFuture.completedFuture(randomPokemon()));
        }
    }

    private int givenRandomInfoProvidingID() {
        GenerationInfo info = randomGenerationInfo();
        when(pokemonApi.getGenerationInfo(anyInt())).thenReturn(CompletableFuture.completedFuture(info));
        return info.getId();
    }

    @Test
    @Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
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
