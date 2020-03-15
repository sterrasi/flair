package org.stepwiselabs.flair.resource;

import org.stepwiselabs.flair.exceptions.ResourceAccessException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class FileSystemReadableResource extends AbstractReadableResource {

    private final Path path;

    FileSystemReadableResource(Path path) {
        super(path.toString());
        this.path = path;
    }

    @Override
    public InputStream open() {
        try {
            return Files.newInputStream(path, StandardOpenOption.READ);
        } catch (IOException e) {
            throw ResourceAccessException.build("Error opening file")
                    .withCause(e)
                    .withParam("resource", location)
                    .build();
        }
    }

    @Override
    public ReadableResource resolve(String relativePath) {

        return ReadableResourceLoader.load(path.resolveSibling(relativePath).toString());
    }

    @Override
    public ReadableResource resolve(Path relativePath) {

        return ReadableResourceLoader.load(path.resolveSibling(relativePath).toString());
    }
}
