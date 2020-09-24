package agnesm.dev.apis.models;

import agnesm.dev.helpers.PokemonHelper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicData extends PokemonHelper {

    private final String name;
    private final String url;

    @JsonCreator
    public BasicData(@JsonProperty(value = "name", required = true) String name,
                     @JsonProperty(value = "url", required = true) String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return extractId(url);
    }
}
