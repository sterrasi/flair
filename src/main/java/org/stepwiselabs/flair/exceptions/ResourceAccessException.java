package org.stepwiselabs.flair.exceptions;

import java.util.Map;

/**
 * Exception that occurs when there is an error in the processing of a resource.
 */
public class ResourceAccessException extends AppException {

    private static final long serialVersionUID = -2019627818348977662L;

    /**
     * {@inheritDoc AppException#AppException(String, Object...)}
     */
    public ResourceAccessException(String format, Object... args) {
        super(format, args);
    }

    /**
     * {@inheritDoc AppException#AppException(Throwable, String, Object...) }
     */
    public ResourceAccessException(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

    private ResourceAccessException(Throwable cause, Map<String, String> params, String format, Object... args) {
        super(cause, params, format, args);
    }

    public static ResourceAccessException create(String format, Object... args) {
        return new ResourceAccessException(format, args);
    }

    public static ResourceAccessException create(Throwable t, String format, Object... args) {
        return new ResourceAccessException(t, format, args);
    }

    public static ExceptionBuilder build(final String format, final Object... args) {
        return new ExceptionBuilder<ResourceAccessException>((cause, params) -> new ResourceAccessException(cause, params, format, args));
    }
}
