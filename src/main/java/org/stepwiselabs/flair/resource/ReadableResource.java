package org.stepwiselabs.flair.resource;

import org.stepwiselabs.flair.functions.ConsumerWithIOException;
import org.stepwiselabs.flair.functions.FunctionWithIOException;

import java.io.InputStream;
import java.nio.file.Path;

public interface ReadableResource {

    String getLocation();

    InputStream open();

    <T> T withResource(FunctionWithIOException<InputStream, T> callback);

    void useResource(ConsumerWithIOException<InputStream> consumer);

    String readContents();

    ReadableResource resolve(String path);

    ReadableResource resolve(Path path);
}
