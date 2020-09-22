package agnesm.dev.apis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoByTypeResponse {

    @JsonProperty("pokemon")
    private List<PokemonResource> pokemon;

    @JsonProperty("moves")
    private List<BasicData> moves;

    @JsonProperty("pokemon")
    public List<PokemonResource> getPokemon() {
        return pokemon;
    }

    @JsonProperty("pokemon")
    public void setPokemon(List<PokemonResource> pokemon) {
        this.pokemon = pokemon;
    }

    @JsonProperty("moves")
    public List<BasicData> getMoves() {
        return moves;
    }

    @JsonProperty("moves")
    public void setMoves(List<BasicData> moves) {
        this.moves = moves;
    }
}
