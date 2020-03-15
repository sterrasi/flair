package org.stepwiselabs.flair.impl;

import org.stepwiselabs.flair.Strings;

import java.util.Optional;
import java.util.Properties;

public class ConfigurationStore {

    private final String prefix;
    private final Properties props;

    public ConfigurationStore(Properties props) {
        this("", props);
    }

    public ConfigurationStore(String prefix, Properties props) {
        this.prefix = prefix;
        this.props = props;
    }

    public Optional<String> getString(String key) {
        if (Strings.isBlank(key)) {
            return Optional.empty();
        }
        return Optional.ofNullable(props.getProperty(getPrefixedKey(key)));
    }

    public Optional<ConfigurationStore> getConfig(String prefix) {

        String newPrefix = getPrefixedKey(prefix);
        boolean hasKeys = props.keySet().stream().anyMatch(k -> ((String) k).startsWith(newPrefix));
        return hasKeys ? Optional.of(new ConfigurationStore(newPrefix, props)) : Optional.empty();
    }

    private String getPrefixedKey(String suffix) {
        return Strings.isBlank(prefix) ? suffix : prefix + "." + suffix;
    }
}
