package agnesm.dev.apis.impl;

import agnesm.dev.apis.PokemonApiClient;
import agnesm.dev.helpers.PokemonHelper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpRequest;

@Component
class PokemonApiClientImpl extends PokemonHelper implements PokemonApiClient {

    @Override
    public HttpRequest getPokemonByGen(int gen) {
        return HttpRequest.newBuilder()
                .uri(URI.create(host + "/generation/" + gen))
                .header("Accept", "application/json")
                .build();
    }

    @Override
    public HttpRequest getPokemonById(int id) {
        return HttpRequest.newBuilder()
                .uri(URI.create(host + "/pokemon/" + id))
                .header("Accept", "application/json")
                .build();
    }

    @Override
    public HttpRequest getMovesByType(String type) {
        return HttpRequest.newBuilder()
                .uri(URI.create(host + "/type/" + type))
                .header("Accept", "application/json")
                .build();
    }
}
