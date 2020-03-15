package org.stepwiselabs.flair;

import java.util.Collection;

public class Preconditions {

    private Preconditions() {
        throw new InstantiationError();
    }

    /**
     * Asserts that the given {@code assertion} is <code>true</code>.  If not, an
     * {@link java.lang.IllegalArgumentException} will be thrown.  The error message is constructed
     * by passing {@code format} and {@code formatArgs} to
     * {@link String#format(String, Object...)}.
     *
     * @param assertion
     * @param format
     * @param formatArgs
     */
    public static void checkArg(boolean assertion, String format, Object... formatArgs) {
        if (!assertion) {
            throw new IllegalArgumentException(String.format(format, formatArgs));
        }
    }

    /**
     * Asserts that the given character {@code sequence} does not consist of only zero or more whitespace
     * characters.  If not, an {@link java.lang.IllegalArgumentException} will be thrown.  The error message is constructed
     * by passing {@code format} and {@code formatArgs} to
     * {@link String#format(String, Object...)}.
     *
     * @param sequence
     * @param desc
     */
    public static void checkNotBlank(final CharSequence sequence, String desc) {
        if (Strings.isBlank(sequence)) {
            throw new IllegalArgumentException(String.format("%s cannot be blank.", desc));
        }
    }

    /**
     * Asserts that the given {@code subject} is not <tt>null</tt>.  If not, an
     * {@link java.lang.IllegalArgumentException} will be thrown.  The error message is constructed
     * by passing {@code format} and {@code formatArgs} to {@link String#format(String, Object...)}.
     *
     * @param subject
     * @param desc
     */
    public static void checkNotNull(final Object subject, String desc) {
        if (subject == null) {
            throw new IllegalArgumentException(String.format("%s cannot be null.", desc));
        }
    }

    /**
     * Asserts that the given {@code collection} is not empty.  If not, an
     * {@link java.lang.IllegalArgumentException} will be thrown.  The error message is constructed
     * by passing {@code format} and {@code formatArgs} to {@link String#format(String, Object...)}.
     *
     * @param collection
     * @param desc
     */
    public static void checkNotEmpty(final Collection<?> collection, String desc) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s cannot be empty.", desc));
        }
    }

}