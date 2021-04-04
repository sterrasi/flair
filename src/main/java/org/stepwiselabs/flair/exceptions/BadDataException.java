package org.stepwiselabs.flair.exceptions;

import java.util.Map;

/**
 * Exception that occurs when passed data does not meet mandatory requirements
 */
public class BadDataException extends AppException {

    private static final long serialVersionUID = -2019627818348977322L;

    /**
     * {@inheritDoc AppException#AppException(String, Object...)}
     */
    public BadDataException(String format, Object... args) {
        super(format, args);
    }

    /**
     * {@inheritDoc AppException#AppException(Throwable, String, Object...) }
     */
    public BadDataException(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

    private BadDataException(Throwable cause, Map<String, String> params, String format, Object... args) {
        super(cause, params, format, args);
    }

    public static BadDataException create(String format, Object... args) {
        return new BadDataException(format, args);
    }

    public static BadDataException create(Throwable t, String format, Object... args) {
        return new BadDataException(t, format, args);
    }

    public static ExceptionBuilder build(final String format, final Object... args) {
        return new ExceptionBuilder<BadDataException>((cause, params) -> new BadDataException(cause, params, format, args));
    }
}
