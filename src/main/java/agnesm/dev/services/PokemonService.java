package agnesm.dev.services;

import agnesm.dev.models.Pokemon;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface PokemonService {

    CompletableFuture<List<Pokemon>> generateRandomizedTeam(int gen, boolean moves);
}
