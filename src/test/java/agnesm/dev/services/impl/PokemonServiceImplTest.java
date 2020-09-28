package agnesm.dev.services.impl;

import agnesm.dev.TestUtil;
import agnesm.dev.apis.PokemonApi;
import agnesm.dev.helpers.PokemonHelper;
import agnesm.dev.models.GenerationInfo;
import agnesm.dev.models.Pokemon;
import agnesm.dev.models.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

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

    @Test
    // async is about being fast, so have a long timeout for failures only and repeatedly and quickly check for success
    @Timeout(value = 200)
    public void shouldGenerateRandomTeamWithMoves() throws Exception {
        int infoID = givenRandomInfoProvidingID();
        givenRandomPokemonTeam();
        givenPokemonApiProvidesRandomMoves();

        CompletableFuture<List<Pokemon>> result = pokemonService.generateRandomizedTeam(infoID, true);
        waitUntilDone(result);

        List<Pokemon> pokemon = result.get();
        Assertions.assertAll( // assertAll generates one error messages from all potential failures, so on a failure you don't have a tree hiding the forest, you get the whole picture at once.
                () -> assertThat(result.isDone()),
                () -> assertThat(pokemon.size()).isEqualTo(PokemonHelper.POKEMON_TEAM_SIZE),
                () -> assertThat(pokemon.stream()).noneMatch(p -> p.getMoves().isEmpty()),
                () -> verify(pokemonApi).getGenerationInfo(infoID),
                () -> verify(pokemonApi, times(PokemonHelper.POKEMON_TEAM_SIZE)).getPokemonById(anyInt()),
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

    @Test
    @Timeout(value = 200)
    public void generateRandomizedTeamSucceedsWithNoMoves() throws Exception {
        int infoID = givenRandomInfoProvidingID();
        givenRandomPokemonTeam();

        CompletableFuture<List<Pokemon>> result = pokemonService.generateRandomizedTeam(infoID, false);
        waitUntilDone(result);

        List<Pokemon> pokemon = result.get();
        Assertions.assertAll(
                () -> assertThat(result.isDone()),
                () -> assertThat(pokemon.size()).isEqualTo(PokemonHelper.POKEMON_TEAM_SIZE),
                () -> assertThat(pokemon.stream()).allMatch(p -> p.getMoves() == null),
                () -> verify(pokemonApi).getGenerationInfo(infoID),
                () -> verify(pokemonApi, times(PokemonHelper.POKEMON_TEAM_SIZE)).getPokemonById(anyInt()),
                () -> verify(pokemonApi, never()).getMovesByType(any(Type.class))
        );
    }

    private void givenPokemonApiProvidesRandomMoves() {
        // answer is a lambda invoked at each call
        when(pokemonApi.getMovesByType(any(Type.class))).thenAnswer((T) -> CompletableFuture.completedFuture(randomMoves()));
    }

    // Pokémon is invariable :)
    private void givenRandomPokemonTeam() {
        // 6 is the number of Pokémon in a team, stocked in a variable in PokemonHelper
        for (int i = 0; i < PokemonHelper.POKEMON_TEAM_SIZE; i++) {
            when(pokemonApi.getPokemonById(anyInt())).thenReturn(CompletableFuture.completedFuture(randomPokemon()));
        }
    }

    private int givenRandomInfoProvidingID() {
        GenerationInfo info = randomGenerationInfo();
        when(pokemonApi.getGenerationInfo(anyInt())).thenReturn(CompletableFuture.completedFuture(info));
        return info.getId();
    }

    private <T> void waitUntilDone(CompletableFuture<T> future) throws InterruptedException {
        while (!future.isDone()) {
            Thread.sleep(2);
        }
    }
}
