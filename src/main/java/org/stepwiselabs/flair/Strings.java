package org.stepwiselabs.flair;

import org.stepwiselabs.flair.exceptions.ResourceAccessException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Static utility methods related to {@link java.lang.String strings}.
 */
public final class Strings {

    // static class
    private Strings() {
        throw new InstantiationError();
    }

    /**
     * Determines if the given {@code sequence} is 'blank'. Blank is defined as
     * being either {@code null} or containing all whitespace characters as
     * defined by {@link Character#isWhitespace(char)}.
     *
     * @param sequence
     * @return {@code true} if the given {@code sequence} is blank.
     */
    public static boolean isBlank(final CharSequence sequence) {
        int sequenceLength;
        if (sequence == null || (sequenceLength = sequence.length()) == 0) {
            return true;
        }
        for (int i = 0; i < sequenceLength; i++) {
            if (Character.isWhitespace(sequence.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Reads the contents of the provided {@link InputStream} into a String. The
     * {@link InputStream} does not get closed.
     *
     * @param in - The {@link InputStream} to read from
     * @return The contents from the {@link InputStream} as a {@link String}.
     * @throws ResourceAccessException when there is an IO error while reading
     */
    public static String readContents(InputStream in) {

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (in, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
            return textBuilder.toString();

        } catch (IOException e) {
            throw new ResourceAccessException(e, e.getMessage());
        }
    }

    /**
     * Converts a {@link String} to an {@link InputStream}.
     *
     * @param toConvert
     * @return
     */
    public static InputStream toInputStream(String toConvert) {
        return new ByteArrayInputStream(toConvert.getBytes(Charset.defaultCharset()));
    }

    /**
     * Returns the inverse of {@link #isBlank(CharSequence)}
     *
     * @param sequence
     * @return the inverse of {@link #isBlank(CharSequence)}
     */
    public static boolean notBlank(final CharSequence sequence) {
        return !isBlank(sequence);
    }
}

