package agnesm.dev.helpers;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class StringHelper {

    public String wordsCapitalize(String s) {
        return Arrays.stream(s.split("-")).map(this::capitalize).collect(Collectors.joining(" "));
    }

    public String capitalize(String s) {
        return (s.length() == 0) ? s : s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
