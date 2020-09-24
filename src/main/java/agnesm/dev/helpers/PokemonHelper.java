package agnesm.dev.helpers;

public abstract class PokemonHelper {

    public int extractId(String url) {
        String[] elements = url.split("/");
        return Integer.parseInt(elements[elements.length - 1]);
    }
}
