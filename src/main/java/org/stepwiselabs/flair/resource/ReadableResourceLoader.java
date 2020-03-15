package org.stepwiselabs.flair.resource;

import org.stepwiselabs.flair.Strings;
import org.stepwiselabs.flair.exceptions.ResourceAccessException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.stepwiselabs.flair.resource.ClasspathReadableResource.CLASSPATH_PREFIX;
import static org.stepwiselabs.flair.resource.ClasspathReadableResource.canOpenClasspathLocation;

public class ReadableResourceLoader {

    /**
     * Loads a {@link ReadableResource} from the given {@literal location}.
     *
     * @param location - The location of the Resource
     * @return ReadableResource
     */
    public static ReadableResource load(String location) {

        if (Strings.isBlank(location)) {
            throw new IllegalArgumentException("resource location is required");
        }
        return location.startsWith(CLASSPATH_PREFIX) ?
                loadClasspathLocation(location) :
                loadFileLocation(Paths.get(location));
    }

    public static String readContents(String location) {

        return load(location).withResource(in -> Strings.readContents(in));
    }

    private static ReadableResource loadClasspathLocation(String location) {

        if (!canOpenClasspathLocation(location)) {
            throw ResourceAccessException.build("Cannot open classpath location")
                    .withParam("location", location)
                    .build();
        }
        return new ClasspathReadableResource(location);
    }

    private static ReadableResource loadFileLocation(Path path) {

        if (!Files.exists(path)) {
            throw ResourceAccessException.build("File does not exist")
                    .withParam("path", path.toString()).build();
        }
        if (!Files.isReadable(path)) {
            throw ResourceAccessException.build("File cannot be read from")
                    .withParam("path", path.toString()).build();
        }

        return new FileSystemReadableResource(path);
    }
}
