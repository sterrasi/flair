package org.stepwiselabs.flair.exceptions;

import java.util.Map;

/**
 * Exception that occurs when a required application configuration parameter was not specified or is invalid
 */
public class AppConfigurationException extends AppException {

    private static final long serialVersionUID = -2091627813348977662L;

    /**
     * {@inheritDoc AppException#AppException(String, Object...)}
     */
    public AppConfigurationException(String format, Object... args) {
        super(format, args);
    }

    /**
     * {@inheritDoc AppException#AppException(Throwable, String, Object...) }
     */
    public AppConfigurationException(Throwable cause, String format, Object... args) {
        super(cause, format, args);
    }

    private AppConfigurationException(Throwable cause, Map<String, String> params, String format, Object... args) {
        super(cause, params, format, args);
    }

    public static AppConfigurationException create(String format, Object... args) {
        return new AppConfigurationException(format, args);
    }

    public static AppConfigurationException create(Throwable t, String format, Object... args) {
        return new AppConfigurationException(t, format, args);
    }

    public static ExceptionBuilder build(final String format, final Object... args) {
        return new ExceptionBuilder<AppConfigurationException>((cause, params) -> new AppConfigurationException(cause, params, format, args));
    }
}
