package org.stepwiselabs.flair;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertTrue;

public class ResourceUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtilsTest.class);

    /**
     * The resources root directory path is accessible from the ReadableResource Impl, and should resolve to a
     * readable filesystem resource
     */
    @Test
    public void assertResourceFilePathExists() {

        // given
        Path readableFile = ResourceUtils.getResourcesRoot().resolve("readableResourceTest/readable-resource.txt");

        // then
        LOGGER.info("readable file: " + readableFile);
        assertTrue(Files.isReadable(readableFile));
    }

    @Test
    public void canLoadProperties() {

        Properties props = ResourceUtils.loadProperties(
                "classpath:configurationTest/sample-configuration.properties");
        assertThat(props.getProperty("param.stringValue")).isEqualTo("hello");
    }

    @Test
    public void canLoadConfiguration() {

        Configuration config = ResourceUtils.loadConfiguration(
                "classpath:configurationTest/sample-configuration.properties");
        assertThat(config.getRequiredString("param.stringValue")).isEqualTo("hello");
    }
}
