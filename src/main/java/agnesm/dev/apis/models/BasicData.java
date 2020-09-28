package agnesm.dev.apis.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static agnesm.dev.helpers.PokemonHelper.extractId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicData {

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
