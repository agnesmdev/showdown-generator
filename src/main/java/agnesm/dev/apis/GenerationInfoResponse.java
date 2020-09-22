package agnesm.dev.apis;

import agnesm.dev.models.GenerationInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerationInfoResponse {

    @JsonProperty("id")
    private int id;

    @JsonProperty("pokemon_species")
    private List<BasicData> species;

    @JsonProperty("moves")
    private List<BasicData> moves;

    @JsonProperty("abilities")
    private List<BasicData> abilities;

    public GenerationInfo toGenerationInfo() {
        return new GenerationInfo(
                this.id,
                this.abilities.stream().map(BasicData::getName).collect(Collectors.toList()),
                this.species.stream().map(BasicData::getId).collect(Collectors.toList()),
                this.moves.stream().map(BasicData::getName).collect(Collectors.toList())
        );
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("pokemon_species")
    public List<BasicData> getSpecies() {
        return species;
    }

    @JsonProperty("pokemon_species")
    public void setSpecies(List<BasicData> species) {
        this.species = species;
    }

    @JsonProperty("moves")
    public List<BasicData> getMoves() {
        return moves;
    }

    @JsonProperty("moves")
    public void setMoves(List<BasicData> moves) {
        this.moves = moves;
    }

    @JsonProperty("abilities")
    public List<BasicData> getAbilities() {
        return abilities;
    }

    @JsonProperty("abilities")
    public void setAbilities(List<BasicData> abilities) {
        this.abilities = abilities;
    }

}
