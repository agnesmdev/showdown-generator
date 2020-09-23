package agnesm.dev.apis;

import java.net.http.HttpRequest;

public interface PokemonApiClient {

    HttpRequest getPokemonByGen(int gen);

    HttpRequest getPokemonById(int id);

    HttpRequest getMovesByType(String type);
}
