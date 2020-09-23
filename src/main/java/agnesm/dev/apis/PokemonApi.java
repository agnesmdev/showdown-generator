package agnesm.dev.apis;

import agnesm.dev.models.GenerationInfo;
import agnesm.dev.models.Pokemon;
import agnesm.dev.models.Type;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PokemonApi {

    CompletableFuture<GenerationInfo> getGenerationInfo(int gen);

    CompletableFuture<Pokemon> getPokemonById(int id);

    CompletableFuture<List<String>> getMovesByType(Type type);
}
