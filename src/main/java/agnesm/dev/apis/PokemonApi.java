package agnesm.dev.apis;

import agnesm.dev.models.GenerationInfo;
import agnesm.dev.models.Pokemon;
import agnesm.dev.exceptions.ApiException;
import agnesm.dev.exceptions.JsonException;
import agnesm.dev.models.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public interface PokemonApi {

    CompletableFuture<GenerationInfo> getPokemonByGeneration(int gen);

    CompletableFuture<Pokemon> getPokemonById(int id);

    CompletableFuture<List<String>> getMovesByType(Type type);
}

@Component
class PokemonApiImpl implements PokemonApi {

    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    @Inject
    private PokemonApiClient client;

    public PokemonApiImpl() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public CompletableFuture<GenerationInfo> getPokemonByGeneration(int gen) {
        HttpRequest request = client.getPokemonByGen(gen);
        return httpClient
                .sendAsync(request, BodyHandlers.ofString())
                .handle((r, t) -> {
                    if (r.statusCode() == 200) {
                        try {
                            GenerationInfoResponse info = objectMapper.readValue(r.body(), GenerationInfoResponse.class);
                            return info.toGenerationInfo();
                        } catch (Exception e) {
                            throw new JsonException(e.getMessage());
                        }
                    } else {
                        throw new ApiException(r.statusCode(), r.body());
                    }
                });
    }

    @Override
    public CompletableFuture<Pokemon> getPokemonById(int id) {
        HttpRequest request = client.getPokemonById(id);
        return httpClient
                .sendAsync(request, BodyHandlers.ofString())
                .handle((r, t) -> {
                    if (r.statusCode() == 200) {
                        try {
                            PokemonByIdResponse response = objectMapper.readValue(r.body(), PokemonByIdResponse.class);
                            return response.toPokemon();
                        } catch (Exception e) {
                            throw new JsonException(e.getMessage());
                        }
                    } else {
                        throw new ApiException(r.statusCode(), r.body());
                    }
                });
    }

    @Override
    public CompletableFuture<List<String>> getMovesByType(Type type) {
        HttpRequest request = client.getMovesByType(type.name());
        return httpClient
                .sendAsync(request, BodyHandlers.ofString())
                .handle((r, t) -> {
                    if (r.statusCode() == 200) {
                        try {
                            InfoByTypeResponse response = objectMapper.readValue(r.body(), InfoByTypeResponse.class);
                            return response.getMoves().stream().map(BasicData::getName).collect(Collectors.toList());
                        } catch (Exception e) {
                            throw new JsonException(e.getMessage());
                        }
                    } else {
                        throw new ApiException(r.statusCode(), r.body());
                    }
                });
    }
}
