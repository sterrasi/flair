package org.stepwiselabs.flair;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Common {@link java.util.Collection} utility methods.
 *
 * @author sterrasi
 */
public class CollectionUtil {

    @SafeVarargs
    public static <T> List<T> arrayList(T... values) {
        List<T> list = new ArrayList<>();
        for (T value : values) {
            list.add(value);
        }
        return list;
    }

    @SafeVarargs
    public static <T> List<T> linkedList(T... values) {
        List<T> list = new LinkedList<>();
        for (T value : values) {
            list.add(value);
        }
        return list;
    }


    public static <T> List<T> arrayList(Collection<T> values) {
        List<T> list = new ArrayList<>();
        for (T value : values) {
            list.add(value);
        }
        return list;
    }


    @SafeVarargs
    public static <T> Set<T> hashSet(T... values) {
        Set<T> set = new HashSet<>();
        for (T value : values) {
            set.add(value);
        }
        return set;
    }

    public static <T> Set<T> hashSet(Collection<T> values) {
        Set<T> set = new HashSet<>();
        for (T value : values) {
            set.add(value);
        }
        return set;
    }

    public static String join(final Collection<?> values, String separator) {
        Preconditions.checkNotNull(values, "values");
        Preconditions.checkNotNull(separator, "separator");

        return values.stream().map(v -> v.toString()).collect(Collectors.joining(separator));
    }

}
