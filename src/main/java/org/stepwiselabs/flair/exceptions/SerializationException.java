package org.stepwiselabs.flair.exceptions;

import java.util.Map;

/**
 * Exception that occurs when transforming an object to a persistent format.
 */
public class SerializationException extends AppException {

    private static final long serialVersionUID = -2091627819848977662L;

    /**
     * {@inheritDoc AppException#AppException(String, Object...)}
     */
    public SerializationException(String format, Object... args) {
        super(format, args);
    }

    /**
     * {@inheritDoc AppException#AppException(Throwable, String, Object...) }
     */
    public SerializationException(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

    private SerializationException(Throwable cause, Map<String, String> params, String format, Object... args) {
        super(cause, params, format, args);
    }

    public static SerializationException create(String format, Object... args) {
        return new SerializationException(format, args);
    }

    public static SerializationException create(Throwable t, String format, Object... args) {
        return new SerializationException(t, format, args);
    }

    public static ExceptionBuilder build(final String format, final Object... args) {
        return new ExceptionBuilder<SerializationException>((cause, params) -> new SerializationException(cause, params, format, args));
    }
}
