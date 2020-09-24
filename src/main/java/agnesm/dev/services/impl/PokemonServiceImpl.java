package agnesm.dev.services.impl;

import agnesm.dev.apis.PokemonApi;
import agnesm.dev.helpers.ListHelper;
import agnesm.dev.models.GenerationInfo;
import agnesm.dev.models.Pokemon;
import agnesm.dev.services.PokemonService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
class PokemonServiceImpl extends ListHelper implements PokemonService {

    @Autowired
    private PokemonApi pokemonApi;

    @Autowired
    private Logger logger;

    @Autowired
    private Random random;

    @Override
    public CompletableFuture<List<Pokemon>> generateRandomizedTeam(int gen, boolean moves) {
        logger.debug("Generating randomized team for gen " + gen + " and " + ((moves) ? "" : "no ") + "moves");
        return pokemonApi.getGenerationInfo(gen)
                .thenCompose(info -> getPokemon(info, moves));
    }

    private CompletableFuture<List<Pokemon>> getPokemon(GenerationInfo info, boolean moves) {
        List<Integer> randomizedIds = randomizedPokemon(info.getPokemon());

        List<CompletableFuture<Pokemon>> pokemon = randomizedIds.stream().map(id ->
                pokemonApi.getPokemonById(id).thenCompose(p -> updatePokemon(info, p, moves))
        ).collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(pokemon.toArray(new CompletableFuture[0]));
        return allFutures.thenApply(future -> pokemon.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    private CompletableFuture<Pokemon> updatePokemon(GenerationInfo info, Pokemon pokemon, boolean moves) {
        if (info.getId() >= 3) {
            pokemon.setAbility(randomizedAbility(info));
        }

        if (!moves) {
            return CompletableFuture.completedFuture(pokemon);
        }

        return updatePokemonMoves(info, pokemon);
    }

    private CompletableFuture<Pokemon> updatePokemonMoves(GenerationInfo info, Pokemon pokemon) {
        CompletableFuture<List<String>> primaryMoves = pokemonApi.getMovesByType(pokemon.getPrimaryType());
        CompletableFuture<List<String>> secondaryMoves = (pokemon.getSecondaryType() == null) ?
                CompletableFuture.completedFuture(Collections.emptyList()) :
                pokemonApi.getMovesByType(pokemon.getSecondaryType());

        return primaryMoves.thenCombine(secondaryMoves, (pm, sm) -> {
            pokemon.setMoves(randomizedMoves(info.getMoves(), concatLists(pm, sm)));
            return pokemon;
        });

    }

    private String randomizedAbility(GenerationInfo info) {
        List<String> abilities = info.getAbilities();
        return abilities.get(random.nextInt(abilities.size()));
    }

    private List<Integer> randomizedPokemon(List<Integer> ids) {
        List<Integer> result = new ArrayList<>();

        while (result.size() < 6) {
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
