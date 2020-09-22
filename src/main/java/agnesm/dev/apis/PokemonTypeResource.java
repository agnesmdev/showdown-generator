package agnesm.dev.apis;

import agnesm.dev.models.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonTypeResource {

    @JsonProperty("type")
    private PokemonTypeData type;

    @JsonProperty("type")
    public PokemonTypeData getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(PokemonTypeData type) {
        this.type = type;
    }

    public Type toType() {
        return Type.valueOf(type.getName());
    }
}
