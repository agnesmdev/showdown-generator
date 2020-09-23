package agnesm.dev.apis.impl;

import agnesm.dev.apis.*;
import agnesm.dev.apis.models.BasicData;
import agnesm.dev.apis.models.GenerationInfoResponse;
import agnesm.dev.apis.models.InfoByTypeResponse;
import agnesm.dev.apis.models.PokemonByIdResponse;
import agnesm.dev.exceptions.ApiException;
import agnesm.dev.exceptions.JsonException;
import agnesm.dev.models.GenerationInfo;
import agnesm.dev.models.Pokemon;
import agnesm.dev.models.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
class PokemonApiImpl implements PokemonApi {

    private Logger logger;
    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    @Inject
    private PokemonApiClient client;

    public PokemonApiImpl(Logger logger) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.logger = logger;
    }

    @Override
    public CompletableFuture<GenerationInfo> getGenerationInfo(int gen) {
        logger.debug("Calling Pokémon API to get information about gen " + gen);

        HttpRequest request = client.getPokemonByGen(gen);
        return httpClient
                .sendAsync(request, BodyHandlers.ofString())
                .handle((r, t) -> {
                    if (r.statusCode() == 200) {
                        try {
                            GenerationInfoResponse info = objectMapper.readValue(r.body(), GenerationInfoResponse.class);

                            logger.debug("Successfully called Pokémon API and got information about gen " + info.getId());
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
        logger.debug("Calling Pokémon API to get pokemon with id " + id);

        HttpRequest request = client.getPokemonById(id);
        return httpClient
                .sendAsync(request, BodyHandlers.ofString())
                .handle((r, t) -> {
                    if (r.statusCode() == 200) {
                        try {
                            PokemonByIdResponse response = objectMapper.readValue(r.body(), PokemonByIdResponse.class);

                            logger.debug("Successfully called Pokémon API and got pokemon " + response.getName() + " with id " + id);
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
        logger.debug("Calling Pokémon API to get moves of type " + type);

        HttpRequest request = client.getMovesByType(type.name());
        return httpClient
                .sendAsync(request, BodyHandlers.ofString())
                .handle((r, t) -> {
                    if (r.statusCode() == 200) {
                        try {
                            InfoByTypeResponse response = objectMapper.readValue(r.body(), InfoByTypeResponse.class);
                            logger.debug("Successfully called Pokémon API and got " + response.getMoves().size() + " moves for type " + type);

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
