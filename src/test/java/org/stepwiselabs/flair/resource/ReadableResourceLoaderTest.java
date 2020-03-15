package org.stepwiselabs.flair.resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stepwiselabs.flair.ResourceUtils;

import java.nio.file.Path;

import static com.google.common.truth.Truth.assertThat;

public class ReadableResourceLoaderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadableResourceLoaderTest.class);
    private static final Path readableFile = ResourceUtils.getResourcesRoot().resolve(
            "readableResourceTest/readable-resource.txt");

    private static final String CLASSPATH_RESOURCE_PATH = "classpath:readableResourceTest/readable-resource.txt";
    private static final String SUB_RESOURCE = "subDir/sub-dir-readable-resource.txt";
    private static final String CONTENTS = "Hello Readable!";
    private static final String SUB_CONTENTS = "Hello Sub Dir Readable!";

    @Test
    public void resourceExists() {
        readableFile.toFile().exists();
    }

    @Test
    public void canLoadFileSystemResource() {
        String contents = ReadableResourceLoader.readContents(readableFile.toString());

        LOGGER.info("contents = " + contents);
        assertThat(contents).isEqualTo(CONTENTS);
    }

    @Test
    public void canLoadClasspathResource() {
        String contents = ReadableResourceLoader.readContents(CLASSPATH_RESOURCE_PATH);
        assertThat(contents).isEqualTo(CONTENTS);
    }

    @Test
    public void canGetLocation() {
        ReadableResource classpathResource = ReadableResourceLoader.load(CLASSPATH_RESOURCE_PATH);
        assertThat(classpathResource.getLocation()).isEqualTo(CLASSPATH_RESOURCE_PATH);

        ReadableResource fileResoource = ReadableResourceLoader.load(readableFile.toString());
        assertThat(fileResoource.getLocation()).isEqualTo(readableFile.toString());
    }

    @Test
    public void classpathResource_canResolveSubdir() {

        ReadableResource classpathResource = ReadableResourceLoader.load(CLASSPATH_RESOURCE_PATH);
        ReadableResource subResource = classpathResource.resolve(SUB_RESOURCE);
        assertThat(subResource.readContents()).isEqualTo(SUB_CONTENTS);
    }

    @Test
    public void fileSystemResource_canResolveSubdir() {

        ReadableResource classpathResource = ReadableResourceLoader.load(readableFile.toString());
        ReadableResource subResource = classpathResource.resolve(SUB_RESOURCE);
        assertThat(subResource.readContents()).isEqualTo(SUB_CONTENTS);
    }
}
