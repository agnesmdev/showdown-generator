package agnesm.dev.apis.models;

import agnesm.dev.models.Pokemon;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonByIdResponse {

    private final BasicData species;
    private final String name;
    private final List<PokemonTypeResource> types;

    @JsonCreator
    public PokemonByIdResponse(@JsonProperty(value = "species", required = true) BasicData species,
                               @JsonProperty(value = "name", required = true) String name,
                               @JsonProperty(value = "types", required = true) List<PokemonTypeResource> types) {
        this.species = species;
        this.name = name;
        this.types = types;
    }

    public Pokemon toPokemon() {
        return new Pokemon(species.getId(), name, types.get(0).toType(), (types.size() > 1) ? types.get(1).toType() : null);
    }

    public String getName() {
        return name;
    }

}
