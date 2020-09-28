package agnesm.dev.services.impl;

import agnesm.dev.apis.PokemonApi;
import agnesm.dev.helpers.ListHelper;
import agnesm.dev.helpers.PokemonHelper;
import agnesm.dev.models.GenerationInfo;
import agnesm.dev.models.Pokemon;
import agnesm.dev.services.PokemonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
class PokemonServiceImpl implements PokemonService {

    private final Logger logger = LoggerFactory.getLogger(PokemonServiceImpl.class);

    private final PokemonApi pokemonApi;

    private final Random random;

    @Autowired
    public PokemonServiceImpl(PokemonApi pokemonApi, Random random) {
        this.pokemonApi = pokemonApi;
        this.random = random;
    }

    @Override
    public CompletableFuture<List<Pokemon>> generateRandomizedTeam(int gen, boolean moves) {
        logger.debug("Generating randomized team for gen " + gen + " with " + ((moves) ? "" : "no ") + "moves");
        return getGenerationInfo(gen, new ArrayList<>()).thenCompose(info -> getPokemon(info, gen, moves));
    }

    private CompletableFuture<List<GenerationInfo>> getGenerationInfo(int gen, List<GenerationInfo> info) {
        if (gen == 0) {
            return CompletableFuture.completedFuture(info);
        }

        return pokemonApi.getGenerationInfo(gen).thenCompose(i -> {
            info.add(i);
            return getGenerationInfo(gen - 1, info);
        });
    }

    private CompletableFuture<List<Pokemon>> getPokemon(List<GenerationInfo> info, int gen, boolean moves) {
        List<Integer> randomizedIds = randomizedPokemon(ListHelper.flatMap(info, GenerationInfo::getPokemon));
        return ListHelper.traverse(randomizedIds,
                id -> pokemonApi.getPokemonById(id).thenCompose(p -> updatePokemon(p, gen, moves, info)),
                new ArrayList<>()
        );
    }

    private CompletableFuture<Pokemon> updatePokemon(Pokemon pokemon, int gen, boolean moves, List<GenerationInfo> info) {
        if (gen >= 3) {
            pokemon.setAbility(randomizedAbility(ListHelper.flatMap(info, GenerationInfo::getAbilities)));
        }

        if (!moves) {
            return CompletableFuture.completedFuture(pokemon);
        }

        return updatePokemonMoves(ListHelper.flatMap(info, GenerationInfo::getMoves), pokemon);
    }

    private CompletableFuture<Pokemon> updatePokemonMoves(List<String> moves, Pokemon pokemon) {
        CompletableFuture<List<String>> primaryMoves = pokemonApi.getMovesByType(pokemon.getPrimaryType());
        CompletableFuture<List<String>> secondaryMoves = (pokemon.getSecondaryType() == null) ?
                CompletableFuture.completedFuture(Collections.emptyList()) :
                pokemonApi.getMovesByType(pokemon.getSecondaryType());

        return primaryMoves.thenCombine(secondaryMoves, (pm, sm) -> {
            pokemon.setMoves(randomizedMoves(moves, ListHelper.concatLists(pm, sm)));
            return pokemon;
        });

    }

    private String randomizedAbility(List<String> abilities) {
        return abilities.get(random.nextInt(abilities.size()));
    }

    private List<Integer> randomizedPokemon(List<Integer> ids) {
        List<Integer> result = new ArrayList<>();

        while (result.size() < PokemonHelper.POKEMON_TEAM_SIZE) {
            int id = ids.remove(random.nextInt(ids.size()));
            result.add(id);
        }

        return result;
    }

    private List<String> randomizedMoves(List<String> moves, List<String> favoriteMoves) {
        List<String> result = new ArrayList<>();

        while (result.size() < 4) {
            String move = (random.nextInt() % 2 == 0) ?
                    moves.remove(random.nextInt(moves.size())) :
                    favoriteMoves.remove(random.nextInt(favoriteMoves.size()));
            result.add(move);
        }

        return result;
    }
}
