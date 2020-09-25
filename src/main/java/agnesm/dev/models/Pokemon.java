package agnesm.dev.models;

import agnesm.dev.helpers.StringHelper;

import java.util.List;
import java.util.stream.Collectors;

public class Pokemon extends StringHelper {

    private final int number;
    private final String name;
    private final Type primaryType;
    private final Type secondaryType;
    private List<String> moves;
    private String ability;

    public Pokemon(int number, String name, Type primaryType, Type secondaryType) {
        this.number = number;
        this.name = capitalize(name);
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
    }

    public String toShowdownFormat() {
        return String.format("%s\nAbility: %s\n%s", name, ability, movesToShowdownFormat());
    }

    public String movesToShowdownFormat() {
        if (moves == null) {
            return "\n";
        }

        return moves.stream().map(m -> String.format("- %s", m)).collect(Collectors.joining("\n"));
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
        this.ability = wordsCapitalize(ability);
    }
}
