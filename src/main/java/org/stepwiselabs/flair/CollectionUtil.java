package org.stepwiselabs.flair;

import java.util.*;


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
        Preconditions.checkNotEmpty(values, "values");
        Preconditions.checkNotNull(separator, "separator");

        StringBuilder sb = new StringBuilder();
        for (Object obj : values) {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(obj.toString());
        }
        return sb.toString();
    }

    /**
     * Returns an immutable map from the passed {@link Map}.
     *
     * @param map
     * @return An immutable {@link Map}
     */
    public static <K, V> Map<K, V> toImmutable(Map<K, V> map) {
        Preconditions.checkNotNull(map, "map");
        Map<K, V> clone = new HashMap<>();
        clone.putAll(map);
        return Collections.unmodifiableMap(map);
    }

    /**
     * Returns an immutable list from the passed {@link List}.
     *
     * @param list
     * @return An immutable {@link List}
     */
    public static <T> List<T> toImmutable(List<T> list) {
        Preconditions.checkNotNull(list, "list");
        List<T> clone = new ArrayList<>();
        clone.addAll(list);
        return Collections.unmodifiableList(list);
    }

    /**
     * Returns an immutable {@link Set} from the passed {@link Set}.
     *
     * @param set
     * @return An immutable {@link Set}
     */
    public static <T> Set<T> toImmutable(Set<T> set) {
        Preconditions.checkNotNull(set, "set");
        Set<T> clone = new HashSet<>();
        clone.addAll(set);
        return Collections.unmodifiableSet(clone);
    }
}
