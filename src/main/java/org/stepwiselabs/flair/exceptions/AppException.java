package org.stepwiselabs.flair.exceptions;


import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.function.BiFunction;

import static org.stepwiselabs.flair.Preconditions.checkNotBlank;
import static org.stepwiselabs.flair.Preconditions.checkNotNull;

/**
 * Application level exception.  Most other app level {@linkplain RuntimeException}s that
 * are explicitly thrown extend {@linkplain AppException}.  This allows for the ability
 * to easily roll up exception types.
 *
 * @author sterrasi
 */
public class AppException extends RuntimeException {

    private static final long serialVersionUID = -2091627819848377632L;
    private final Map<String, String> params;

    /**
     * Constructs an {@linkplain AppException}.  The exception message is constructed by passing the given
     * {@literal format} and {@literal args} directly to {@link String#format(String, Object...)} to create the
     * exception message.
     *
     * @param format A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.  If there are more arguments than format specifiers, the
     *               extra arguments are ignored.  The number of arguments is
     *               variable and may be zero.
     * @throws IllegalFormatException If a format string contains an illegal syntax, a format
     *                                specifier that is incompatible with the given arguments,
     *                                insufficient arguments given the format string, or other
     *                                illegal conditions.  For specification of all possible
     *                                formatting errors, see the <a href="../util/Formatter.html#detail">Details</a>
     *                                section of the formatter class specification.
     * @throws NullPointerException   If the <tt>format</tt> is <tt>null</tt>
     */
    public AppException(String format, Object... args) {
        super(String.format(format, args));
        params = new HashMap<>(6);
    }

    /**
     * Constructs an {@linkplain AppException}. The exception message is constructed by passing the given
     * {@code format} and {@code args} directly to {@link String#format(String, Object...)} to create the
     * exception message.
     * @param cause  {@link Throwable Cause} of the {@code Exception}.
     * @param format A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.  If there are more arguments than format specifiers, the
     *               extra arguments are ignored.  The number of arguments is
     *               variable and may be zero.
     * @throws IllegalFormatException If a format string contains an illegal syntax, a format
     *                                specifier that is incompatible with the given arguments,
     *                                insufficient arguments given the format string, or other
     *                                illegal conditions.  For specification of all possible
     *                                formatting errors, see the <a
     *                                href="../util/Formatter.html#detail">Details</a> section of the
     *                                formatter class specification.
     * @throws NullPointerException   If the <tt>format</tt> is <tt>null</tt>
     */
    public AppException(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
        params = new HashMap<>();
    }

    /**
     * package-private constructor called by the builder
     *
     * @param cause  {@link Throwable Cause} of the {@code Exception}.
     * @param params A {@link Map} of exception parameters
     * @param format A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.  If there are more arguments than format specifiers, the
     *               extra arguments are ignored.  The number of arguments is
     *               variable and may be zero.
     */
    AppException(Throwable cause, Map<String, String> params, String format, Object... args) {
        super(String.format(format, args), cause);
        this.params = params;
    }

    /**
     * Returns any parameters that were associated with this exception
     *
     * @return a {@link Map} of associated parameters
     */
    public Map<String, String> getParams() {
        return params;
    }

    public static AppException create(String format, Object... args) {
        return new AppException(format, args);
    }

    public static AppException create(Throwable t, String format, Object... args) {
        return new AppException(t, format, args);
    }

    public static ExceptionBuilder build(final String format, final Object... args) {
        return new ExceptionBuilder<AppException>((cause, params) -> new AppException(cause, params, format, args));
    }

    /**
     * Parameterized AppException builder.  It allows for an easy way to add key-value parameters to the
     * exceptions definition.
     *
     * @param <T> {@link AppException} type that this builder produces
     */
    public static class ExceptionBuilder<T extends AppException> {
        private final Map<String, String> params;
        private Throwable cause;
        private final BiFunction<Throwable, Map<String, String>, T> exceptionFactory;

        ExceptionBuilder(BiFunction<Throwable, Map<String, String>, T> exceptionFactory) {
            checkNotNull(exceptionFactory, "exception factory");
            params = new HashMap<>();
            this.exceptionFactory = exceptionFactory;
        }

        public ExceptionBuilder<T> withParam(String key, String value) {
            checkNotBlank(key, "exception parameter key");
            params.put(key, value);
            return this;
        }

        public ExceptionBuilder<T> withCause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public T build() {
            return exceptionFactory.apply(cause, params);
        }
    }
}
