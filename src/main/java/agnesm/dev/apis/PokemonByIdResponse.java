package agnesm.dev.apis;

import agnesm.dev.models.Pokemon;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonByIdResponse {

    @JsonProperty("species")
    private BasicData species;

    @JsonProperty("name")
    private String name;

    @JsonProperty("types")
    private List<PokemonTypeResource> types;

    public Pokemon toPokemon() {
        return new Pokemon(species.getId(), name, types.get(0).toType(), (types.size() > 1) ? types.get(1).toType() : null);
    }

    @JsonProperty("species")
    public BasicData getSpecies() {
        return species;
    }

    @JsonProperty("species")
    public void setSpecies(BasicData species) {
        this.species = species;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("types")
    public List<PokemonTypeResource> getTypes() {
        return types;
    }

    @JsonProperty("types")
    public void setTypes(List<PokemonTypeResource> types) {
        this.types = types;
    }

}
