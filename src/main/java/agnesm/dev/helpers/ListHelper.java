package agnesm.dev.helpers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ListHelper {

    public <T> List<T> concatLists(List<T> l1, List<T> l2) {
        return Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toList());
    }
}
