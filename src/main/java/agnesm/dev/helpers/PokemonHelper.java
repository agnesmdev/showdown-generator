package agnesm.dev.helpers;

public class PokemonHelper {

    public static final int POKEMON_TEAM_SIZE = 6;

    public static int extractId(String url) {
        String[] elements = url.split("/");
        return Integer.parseInt(elements[elements.length - 1]);
    }
}
