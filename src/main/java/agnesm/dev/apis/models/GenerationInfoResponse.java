package agnesm.dev.apis.models;

import agnesm.dev.helpers.ListHelper;
import agnesm.dev.models.GenerationInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerationInfoResponse extends ListHelper {

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
                map(abilities, BasicData::getName),
                map(species, BasicData::getId),
                map(moves, BasicData::getName)
        );
    }

    public int getId() {
        return id;
    }
}
