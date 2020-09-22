package agnesm.dev.models;

import agnesm.dev.helpers.StringHelper;
import agnesm.dev.models.Type;

import java.util.List;
import java.util.stream.Collectors;

public class Pokemon extends StringHelper {

    private int number;
    private String name;
    private Type primaryType;
    private Type secondaryType;
    private List<String> moves;
    private String ability;

    public Pokemon(int number, String name, Type primaryType, Type secondaryType) {
        this.number = number;
        this.name = capitalize(name);
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
    }

    public int getNumber() {
        return this.number;
    }

    public String getName() {
        return this.name;
    }

    public Type getPrimaryType() {
        return this.primaryType;
    }

    public Type getSecondaryType() {
        return this.secondaryType;
    }

    public List<String> getMoves() {
        return this.moves;
    }

    public String getAbility() {
        return this.ability;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves.stream().map(this::wordsCapitalize).collect(Collectors.toList());
    }

    public void setAbility(String ability) {
        this.ability = capitalize(ability);
    }
}
