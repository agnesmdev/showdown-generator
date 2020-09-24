package agnesm.dev.apis.models;

import agnesm.dev.models.GenerationInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerationInfoResponse {

    private final int id;
    private final List<BasicData> species;
    private final List<BasicData> moves;
    private final List<BasicData> abilities;

    @JsonCreator
    public GenerationInfoResponse(@JsonProperty(value = "id", required = true) int id,
                                  @JsonProperty(value = "pokemon_species", required = true) List<BasicData> species,
                                  @JsonProperty(value = "moves", required = true) List<BasicData> moves,
                                  @JsonProperty(value = "abilities", required = true) List<BasicData> abilities) {
        this.id = id;
        this.species = species;
        this.moves = moves;
        this.abilities = abilities;
    }

    public GenerationInfo toGenerationInfo() {
        return new GenerationInfo(
                id,
                abilities.stream().map(BasicData::getName).collect(Collectors.toList()),
                species.stream().map(BasicData::getId).collect(Collectors.toList()),
                moves.stream().map(BasicData::getName).collect(Collectors.toList())
        );
    }

    public int getId() {
        return id;
    }
}
