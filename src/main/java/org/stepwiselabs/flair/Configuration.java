package org.stepwiselabs.flair;

import java.util.List;
import java.util.Optional;

public interface Configuration {

    Optional<String> getString(String key);

    String getString(String key, String defaultValue);

    String getRequiredString(String key);

    List<String> getStringList(String key);

    Optional<Integer> getInteger(String key);

    Integer getInteger(String key, int defaultValue);

    Integer getRequiredInteger(String key);

    List<Integer> getIntegerList(String key);

    Optional<Float> getFloat(String key);

    Float getFloat(String key, float defaultValue);

    Float getRequiredFloat(String key);

    List<Float> getFloatList(String key);

    Optional<Configuration> getSubConfiguration(String prefix);
}
