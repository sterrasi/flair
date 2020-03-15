package org.stepwiselabs.flair;

import org.stepwiselabs.flair.exceptions.ResourceAccessException;
import org.stepwiselabs.flair.impl.ConfigurationImpl;
import org.stepwiselabs.flair.impl.ConfigurationStore;
import org.stepwiselabs.flair.resource.ReadableResource;
import org.stepwiselabs.flair.resource.ReadableResourceLoader;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ResourceUtils {

    /**
     * Returns the path to the resources root directory. This is for the edge case of accessing resources as files.
     *
     * @return the resources root directory path
     */
    public static Path getResourcesRoot() {
        URL rootURL = ResourceUtils.class.getResource("/");
        if (rootURL == null) {
            throw new ResourceAccessException("Unable to find url for resources root");
        }
        String filePath = rootURL.getFile();
        if (filePath == null) {
            throw new ResourceAccessException("Unable to find file directory for resources root");
        }
        return Paths.get(filePath);
    }

    public static Configuration loadConfiguration(String location){
        return loadConfiguration(ReadableResourceLoader.load(location));
    }

    public static Configuration loadConfiguration(ReadableResource resource){
        return new ConfigurationImpl(new ConfigurationStore(loadProperties(resource)));
    }

    public static Properties loadProperties(String location){
        return loadProperties(ReadableResourceLoader.load(location));
    }

    public static Properties loadProperties(ReadableResource resource){
        return resource.withResource( in -> {
            Properties props = new Properties();
            props.load(in);
            return props;
        });
    }
}
