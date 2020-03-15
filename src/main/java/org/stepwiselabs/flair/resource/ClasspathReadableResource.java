package org.stepwiselabs.flair.resource;

import org.stepwiselabs.flair.exceptions.ResourceAccessException;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

class ClasspathReadableResource extends AbstractReadableResource {

    public static final String CLASSPATH_PREFIX = "classpath:";

    ClasspathReadableResource(String location) {
        super(location);
    }

    @Override
    public InputStream open() {
        return openClasspathLocation(location);
    }

    static boolean canOpenClasspathLocation(String location) {
        try (InputStream in = openClasspathLocation(location)) {
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static InputStream openClasspathLocation(String classpathLocation) {

        final String location = stripClasspath(classpathLocation);
        return Stream.of(Thread.currentThread().getContextClassLoader(), ReadableResourceLoader.class.getClassLoader())
                .map(cl -> cl.getResourceAsStream(location))
                .findFirst()
                .orElseThrow(() -> ResourceAccessException.build("Error opening classpath resource")
                        .withParam("location", location)
                        .build());
    }

    private static String stripClasspath(String location) {
        return location.startsWith(CLASSPATH_PREFIX) ? location.substring(CLASSPATH_PREFIX.length()) : location;
    }

    @Override
    public ReadableResource resolve(String relativePath) {

        Path path = Paths.get(stripClasspath(location)).resolveSibling(relativePath);
        return new ClasspathReadableResource(CLASSPATH_PREFIX + path.toString());
    }

    @Override
    public ReadableResource resolve(Path relativePath) {

        Path path = Paths.get(stripClasspath(location)).resolveSibling(relativePath);
        return new ClasspathReadableResource(CLASSPATH_PREFIX + path.toString());
    }
}
