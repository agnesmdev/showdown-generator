package agnesm.dev.models;

import java.util.List;

public class GenerationInfo {

    private int id;
    private List<String> abilities;
    private List<Integer> pokemon;
    private List<String> moves;

    public GenerationInfo(int id, List<String> abilities, List<Integer> pokemon, List<String> moves) {
        this.id = id;
        this.abilities = abilities;
        this.pokemon = pokemon;
        this.moves = moves;
    }

    public int getId() {
        return id;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public List<Integer> getPokemon() {
        return pokemon;
    }

    public List<String> getMoves() {
        return moves;
    }
}
