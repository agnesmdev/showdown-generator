package agnesm.dev.apis.impl;

import agnesm.dev.apis.PokemonApiClient;
import agnesm.dev.conf.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpRequest;

@Component
class PokemonApiClientImpl implements PokemonApiClient {

    @Autowired
    private ConfigProperties properties;

    @Override
    public HttpRequest getPokemonByGen(int gen) {
        return HttpRequest.newBuilder()
                .uri(URI.create(properties.getHost() + "/generation/" + gen))
                .header("Accept", "application/json")
                .build();
    }

    @Override
    public HttpRequest getPokemonById(int id) {
        return HttpRequest.newBuilder()
                .uri(URI.create(properties.getHost() + "/pokemon/" + id))
                .header("Accept", "application/json")
                .build();
    }

    @Override
    public HttpRequest getMovesByType(String type) {
        return HttpRequest.newBuilder()
                .uri(URI.create(properties.getHost() + "/type/" + type))
                .header("Accept", "application/json")
                .build();
    }
}
