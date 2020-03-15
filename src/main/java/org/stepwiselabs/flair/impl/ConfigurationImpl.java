package org.stepwiselabs.flair.impl;

import org.stepwiselabs.flair.Configuration;
import org.stepwiselabs.flair.Strings;
import org.stepwiselabs.flair.exceptions.AppConfigurationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConfigurationImpl implements Configuration {

    private final ConfigurationStore store;

    public ConfigurationImpl(ConfigurationStore store) {
        this.store = store;
    }

    @Override
    public Optional<String> getString(String key) {
        return store.getString(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return store.getString(key).orElse(defaultValue);
    }

    @Override
    public String getRequiredString(String key) {
        return store.getString(key).orElseThrow(() -> AppConfigurationException
                .build("Missing required parameter")
                .withParam("paramName", key)
                .build());
    }

    @Override
    public List<String> getStringList(String key) {
        return store.getString(key).map(this::toStringList).orElse(Collections.emptyList());
    }

    @Override
    public Optional<Integer> getInteger(final String key) {
        return store.getString(key).map(v -> toInteger(key, v));
    }

    @Override
    public Integer getInteger(String key, int defaultValue) {
        return store.getString(key).map(v -> toInteger(key, v)).orElse(defaultValue);
    }

    @Override
    public Integer getRequiredInteger(String key) {
        return store.getString(key).map(v -> toInteger(key, v)).orElseThrow(() -> AppConfigurationException
                .build("Missing required parameter")
                .withParam("paramName", key)
                .build());
    }

    @Override
    public List<Integer> getIntegerList(String key) {
        return store.getString(key).map(v -> toIntegerList(key, v)).orElse(Collections.emptyList());
    }

    @Override
    public Optional<Float> getFloat(String key) {
        return store.getString(key).map(v -> toFloat(key, v));
    }

    @Override
    public Float getFloat(String key, float defaultValue) {
        return store.getString(key).map(v -> toFloat(key, v)).orElse(defaultValue);
    }

    @Override
    public Float getRequiredFloat(String key) {
        return store.getString(key).map(v -> toFloat(key, v)).orElseThrow(() -> AppConfigurationException
                .build("Missing required parameter")
                .withParam("paramName", key)
                .build());
    }

    @Override
    public List<Float> getFloatList(String key) {
        return store.getString(key).map(v -> toFloatList(key, v)).orElse(Collections.emptyList());
    }

    @Override
    public Optional<Configuration> getSubConfiguration(String prefix) {
        return store.getConfig(prefix).map(store -> new ConfigurationImpl(store));
    }

    private List<String> toStringList(String value) {
        if (Strings.isBlank(value)) {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split(","))
                .filter(Strings::notBlank)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private List<Integer> toIntegerList(String key, String value) {
        if (Strings.isBlank(value)) {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split(","))
                .filter(Strings::notBlank)
                .map(v -> toInteger(key, v.trim()))
                .collect(Collectors.toList());
    }


    private List<Float> toFloatList(String key, String value) {
        if (Strings.isBlank(value)) {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split(","))
                .filter(Strings::notBlank)
                .map(v -> toFloat(key, v.trim()))
                .collect(Collectors.toList());
    }

    private Integer toInteger(String key, String value) {
        if (Strings.isBlank(value)) {
            throw AppConfigurationException.build("Blank parameter provided")
                    .withParam("paramName", key)
                    .withParam("paramValue", value)
                    .build();
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw AppConfigurationException.build("Invalid integer parameter provided")
                    .withCause(e)
                    .withParam("paramName", key)
                    .withParam("paramValue", value)
                    .build();
        }
    }

    private Float toFloat(String key, String value) {
        if (Strings.isBlank(value)) {
            throw AppConfigurationException.build("Blank parameter provided")
                    .withParam("paramName", key)
                    .withParam("paramValue", value)
                    .build();
        }
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw AppConfigurationException.build("Invalid float parameter provided")
                    .withCause(e)
                    .withParam("paramName", key)
                    .withParam("paramValue", value)
                    .build();
        }
    }

}
