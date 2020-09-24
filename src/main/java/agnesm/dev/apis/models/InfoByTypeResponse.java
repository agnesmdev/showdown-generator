package agnesm.dev.apis.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoByTypeResponse {

    private final List<BasicData> moves;

    @JsonCreator
    public InfoByTypeResponse(@JsonProperty(value = "moves", required = true) List<BasicData> moves) {
        this.moves = moves;
    }

    public List<BasicData> getMoves() {
        return moves;
    }
}
