package agnesm.dev.apis.models;

import agnesm.dev.models.Type;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonTypeResource {

    private final PokemonTypeData type;

    @JsonCreator
    public PokemonTypeResource(@JsonProperty(value = "type", required = true) PokemonTypeData type) {
        this.type = type;
    }

    public PokemonTypeData getType() {
        return type;
    }

    public Type toType() {
        return Type.valueOf(type.getName());
    }
}
