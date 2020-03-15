package org.stepwiselabs.flair.exceptions;

import java.util.Map;

/**
 * Exception that occurs when an expected entity could not be found
 */
public class NotFoundException extends AppException {

    private static final long serialVersionUID = -2019627818348977662L;

    /**
     * {@inheritDoc AppException#AppException(String, Object...)}
     */
    public NotFoundException(String format, Object... args) {
        super(format, args);
    }

    /**
     * {@inheritDoc AppException#AppException(Throwable, String, Object...) }
     */
    public NotFoundException(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

    private NotFoundException(Throwable cause, Map<String, String> params, String format, Object... args) {
        super(cause, params, format, args);
    }

    public static NotFoundException create(String format, Object... args) {
        return new NotFoundException(format, args);
    }

    public static NotFoundException create(Throwable t, String format, Object... args) {
        return new NotFoundException(t, format, args);
    }

    public static ExceptionBuilder build(final String format, final Object... args) {
        return new ExceptionBuilder<NotFoundException>((cause, params) -> new NotFoundException(cause, params, format, args));
    }
}
