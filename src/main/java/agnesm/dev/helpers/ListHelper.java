package agnesm.dev.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListHelper {

    public static <T> List<T> concatLists(List<T> l1, List<T> l2) {
        return Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toList());
    }

    public static <T, U> CompletableFuture<List<U>> traverse(List<T> l,
                                                             Function<? super T, ? extends CompletableFuture<U>> fun,
                                                             List<U> result) {
        if (l.isEmpty()) {
            return CompletableFuture.completedFuture(result);
        }

        return fun.apply(l.get(0)).thenCompose(r -> {
            result.add(r);
            l.remove(0);
            return traverse(l, fun, result);
        });
    }

    public static <T, U> List<U> flatMap(List<T> l, Function<? super T, ? extends List<U>> fun) {
        return flatten(map(l, fun));
    }

    public static <T, U> List<U> map(List<T> l, Function<? super T, ? extends U> fun) {
        return l.stream().map(fun).collect(Collectors.toList());
    }

    public static <T> List<T> flatten(List<List<T>> l) {
        List<T> result = new ArrayList<>();
        l.forEach(result::addAll);

        return result;
    }
}
