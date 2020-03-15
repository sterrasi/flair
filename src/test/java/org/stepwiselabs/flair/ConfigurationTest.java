package org.stepwiselabs.flair;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

/**
 * Tests the {@link Configuration} access interface
 */
public class ConfigurationTest {

    private static final String CONFIGURATION_RESOURCE = "classpath:configurationTest/sample-configuration.properties";

    private static final Integer INT_VALUE = 11;
    private static final Float FLOAR_VALUE = 1.1f;
    private Configuration config;

    @Before
    public void before() {
        config = ResourceUtils.loadConfiguration(CONFIGURATION_RESOURCE);
    }

    @Test
    public void testGetString() {

        Optional<String> value = config.getString("param.stringValue");
        assertThat(value).isNotNull();
        assertThat(value.isPresent()).isTrue();
        assertThat(value.get()).isEqualTo("hello");
    }

    @Test
    public void testGetRequiredString() {

        String value = config.getRequiredString("param.stringValue");
        assertThat(value).isNotNull();
        assertThat(value).isEqualTo("hello");
    }

    @Test
    public void testGetStringWithDefault() {

        // not found case
        String value = config.getString("param.notFound!", "defaultValue");
        assertThat(value).isNotEmpty();
        assertThat(value).isEqualTo("defaultValue");

        // found case
        value = config.getString("param.stringValue", "defaultValue");
        assertThat(value).isNotEmpty();
        assertThat(value).isEqualTo("hello");
    }

    @Test
    public void testGetStringList(){

        List<String> values = config.getStringList("param.stringList");
        assertThat(values).isNotNull();
        assertThat(values).containsExactly("first", "second", "third");
    }

    @Test
    public void testGetInteger() {

        Optional<Integer> value = config.getInteger("param.intValue");
        assertThat(value).isNotNull();
        assertThat(value.isPresent()).isTrue();
        assertThat(value.get()).isEqualTo(INT_VALUE);
    }

    @Test
    public void testGetRequiredInteger() {

        Integer value = config.getRequiredInteger("param.intValue");
        assertThat(value).isNotNull();
        assertThat(value).isEqualTo(INT_VALUE);
    }

    @Test
    public void testGetIntegerWithDefault() {

        // not found case
        Integer value = config.getInteger("param.notFound!", 13);
        assertThat(value).isNotNull();
        assertThat(value).isEqualTo(13);

        // found case
        value = config.getInteger("param.intValue", 13);
        assertThat(value).isNotNull();
        assertThat(value).isEqualTo(INT_VALUE);
    }

    @Test
    public void testGetIntegerList(){

        List<Integer> values = config.getIntegerList("param.intList");
        assertThat(values).isNotNull();
        assertThat(values).containsExactly(1, 2, 3);
    }

    @Test
    public void testGetFloat() {

        Optional<Float> value = config.getFloat("param.floatValue");
        assertThat(value).isNotNull();
        assertThat(value.isPresent()).isTrue();
        assertThat(value.get()).isEqualTo(FLOAR_VALUE);
    }

    @Test
    public void testGetRequiredFloat() {

        Float value = config.getRequiredFloat("param.floatValue");
        assertThat(value).isNotNull();
        assertThat(value).isEqualTo(FLOAR_VALUE);
    }

    @Test
    public void testGetFloatWithDefault() {

        // not found case
        Float value = config.getFloat("param.notFound!", 13.13f);
        assertThat(value).isNotNull();
        assertThat(value).isEqualTo(13.13f);

        // found case
        value = config.getFloat("param.floatValue", 13.13f);
        assertThat(value).isNotNull();
        assertThat(value).isEqualTo(FLOAR_VALUE);
    }

    @Test
    public void testGetFloatList(){

        List<Float> values = config.getFloatList("param.floatList");
        assertThat(values).isNotNull();
        assertThat(values).containsExactly(1.1f, 2.2f, 3.3f);
    }

    @Test
    public void testGetSubConfiguration(){
        Optional<Configuration> optSubConfig = config.getSubConfiguration("param.sub");
        assertThat(optSubConfig).isNotNull();
        assertThat(optSubConfig.isPresent()).isTrue();

        Configuration subConfig = optSubConfig.get();
        assertThat(subConfig.getRequiredInteger("intValue")).isEqualTo(22);
        assertThat(subConfig.getRequiredString("stringValue")).isEqualTo("sub.string");
    }

}
