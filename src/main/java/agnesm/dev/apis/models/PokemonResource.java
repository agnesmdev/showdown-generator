package agnesm.dev.apis.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonResource {

    @JsonProperty("pokemon")
    private BasicData pokemon;

    @JsonProperty("pokemon")
    public BasicData getPokemon() {
        return pokemon;
    }

    @JsonProperty("pokemon")
    public void setPokemon(BasicData pokemon) {
        this.pokemon = pokemon;
    }
}
