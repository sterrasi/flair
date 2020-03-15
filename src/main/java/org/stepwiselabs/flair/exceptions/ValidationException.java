package org.stepwiselabs.flair.exceptions;

import java.util.Map;

public class ValidationException extends AppException {

    private static final long serialVersionUID = -3042686053652247285L;

    /**
     * {@inheritDoc AppException#AppException(String, Object...)}
     */
    public ValidationException(String format, Object... args) {
        super(format, args);
    }

    /**
     * {@inheritDoc AppException#AppException(Throwable, String, Object...) }
     */
    public ValidationException(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

    private ValidationException(Throwable cause, Map<String, String> params, String format, Object... args) {
        super(cause, params, format, args);
    }

    public static ValidationException create(String format, Object... args) {
        return new ValidationException(format, args);
    }

    public static ValidationException create(Throwable t, String format, Object... args) {
        return new ValidationException(t, format, args);
    }

    public static ExceptionBuilder build(final String format, final Object... args) {
        return new ExceptionBuilder<ValidationException>((cause, params) -> new ValidationException(cause, params, format, args));
    }
}
