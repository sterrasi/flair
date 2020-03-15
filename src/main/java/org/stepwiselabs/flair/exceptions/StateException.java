package org.stepwiselabs.flair.exceptions;

import java.util.Map;

public class StateException extends AppException {

    private static final long serialVersionUID = -2091623619848977662L;

    /**
     * {@inheritDoc AppException#AppException(String, Object...)}
     */
    public StateException(String format, Object... args) {
        super(format, args);
    }

    /**
     * {@inheritDoc AppException#AppException(Throwable, String, Object...) }
     */
    public StateException(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

    private StateException(Throwable cause, Map<String, String> params, String format, Object... args) {
        super(cause, params, format, args);
    }

    public static StateException create(String format, Object... args) {
        return new StateException(format, args);
    }

    public static StateException create(Throwable t, String format, Object... args) {
        return new StateException(t, format, args);
    }

    public static ExceptionBuilder build(final String format, final Object... args) {
        return new ExceptionBuilder<StateException>((cause, params) -> new StateException(cause, params, format, args));
    }

}