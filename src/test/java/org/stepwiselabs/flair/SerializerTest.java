package org.stepwiselabs.flair;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@SuppressWarnings("javadoc")
public class SerializerTest {

    private static final Person JOE = new Person("Joe", "Shmoe");
    private static final Person ALICE = new Person("Alice", "Jones");
    private static final List<Person> PEOPLE = new ArrayList<>();
    private static ObjectMapper OBJ_MAPPER = new ObjectMapper();

    static {
        PEOPLE.add(JOE);
        PEOPLE.add(ALICE);
    }

    private static final Serializer<Person> PERSON_SERIALIZER = Serializer
            .create(OBJ_MAPPER, Person.class);
    private static final Serializer<ObjectWithEnum> ENUM_SERIALIZER = Serializer
            .create(OBJ_MAPPER, ObjectWithEnum.class);


    /**
     * Test serialization/deserialization of a simple bean.
     *
     * @throws Exception
     */
    @Test
    public void simpleBean() throws Exception {
        // given
        Person deserialized = PERSON_SERIALIZER.deserialize(
                PERSON_SERIALIZER.serialize(JOE));

        // then
        assertJoe(deserialized);
    }

    /**
     * Test serialization/deserialization of a simple bean from an
     * <tt>InputStream</tt>.
     *
     * @throws Exception
     */
    @Test
    public void simpleBeanFromStream() throws Exception {
        // given
        Person deserialized = null;

        try (InputStream in = Strings.toInputStream(PERSON_SERIALIZER.serialize(JOE))) {
            deserialized = PERSON_SERIALIZER.deserialize(in);
        }

        // then
        assertJoe(deserialized);
    }

    /**
     * Test serialization/deserialization for a <tt>List</tt> of simple beans.
     *
     * @throws Exception
     */
    @Test
    public void listOfSimpleBeans() throws Exception {
        // given
        List<Person> deserialized = PERSON_SERIALIZER
                .deserializeList(PERSON_SERIALIZER.serializeList(PEOPLE));

        // then
        assertPeople(deserialized);
    }

    /**
     * Test serialization/deserialization for a <tt>List</tt> of simple beans
     * from an <tt>InputStream</tt>.
     *
     * @throws Exception
     */
    @Test
    public void listOfSimpleBeansFromStream() throws Exception {
        // given
        List<Person> deserialized;
        try (InputStream in = Strings.toInputStream(PERSON_SERIALIZER.serializeList(PEOPLE))) {
            deserialized = PERSON_SERIALIZER.deserializeList(in);
        }

        // then
        assertPeople(deserialized);
    }

    /**
     * Test serialization/deserialization of an annotated enum.
     *
     * @throws Exception
     */
    @Test
    public void annotatedEnum() throws Exception {
        // given
        ObjectWithEnum owe = new ObjectWithEnum();
        owe.setExclusivity(Exclusivity.EXCLUSIVE);
        owe = ENUM_SERIALIZER.deserialize(ENUM_SERIALIZER.serialize(owe));

        // then
        assertThat(Exclusivity.EXCLUSIVE).isEqualTo(owe.getExclusivity());
    }

    /**
     * Handle generics through the use of <tt>JsonTypeInfo</tt>
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void parameterizedMember() throws Exception {

        // given
        @SuppressWarnings({"rawtypes"})
        Serializer<ParameterizedObject> serializer = Serializer
                .create(OBJ_MAPPER, ParameterizedObject.class);

        ParameterizedObject<Person> po = new ParameterizedObject<Person>();
        po.setContent(JOE);
        po = serializer.deserialize(serializer.serialize(po));

        // then
        assertJoe(po.getContent());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void parameterizedListMember() throws Exception {

        @SuppressWarnings("rawtypes")
        Serializer<ObjectWithParameterizedList> serializer = Serializer
                .create(OBJ_MAPPER, ObjectWithParameterizedList.class);

        // create an entity set of person beans
        ObjectWithParameterizedList<Person> obj = new ObjectWithParameterizedList<Person>();
        obj.setResults(PEOPLE);
        obj = serializer.deserialize(serializer.serialize(obj));

        // then
        assertPeople(obj.getResults());
    }

    @Test
    public void testAnnotatedConstructor() throws Exception {

        Serializer<ObjectWithAnnotatedConstructor> serializer = Serializer
                .create(OBJ_MAPPER, ObjectWithAnnotatedConstructor.class);

        ObjectWithAnnotatedConstructor obj = new ObjectWithAnnotatedConstructor(
                "steve");

        // serialize and de-serialize
        String result = serializer.serialize(obj);
        obj = serializer.deserialize(result);

        assertThat("steve").isEqualTo(obj.getName());
    }

    @Test
    public void testSerializeAsBytes() throws Exception {

        Serializer<ObjectWithAnnotatedConstructor> serializer = Serializer
                .create(OBJ_MAPPER, ObjectWithAnnotatedConstructor.class);

        ObjectWithAnnotatedConstructor obj = new ObjectWithAnnotatedConstructor(
                "steve");

        // serialize and de-serialize
        byte[] result = serializer.serializeAsBytes(obj);
        assertThat(result).isEqualTo(
                "{\"name\":\"steve\"}".getBytes(Charset.defaultCharset())
        );

        obj = serializer.deserialize(new ByteArrayInputStream(result));
        assertThat(obj.getName()).isEqualTo("steve");
    }

    private static void assertPeople(List<Person> actual) {
        assertThat(actual).hasSize(PEOPLE.size());
        assertJoe(actual.get(0));
        assertAlice(actual.get(1));
    }

    private static void assertAlice(Person actual) {
        assertThat(actual).isNotNull();
        assertThat(actual.getFirstName()).isEqualTo(ALICE.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(ALICE.getLastName());
    }

    private static void assertJoe(Person actual) {
        assertThat(actual).isNotNull();
        assertThat(actual.getFirstName()).isEqualTo(JOE.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(JOE.getLastName());
    }

    // for objects without setters
    public static class ObjectWithAnnotatedConstructor {
        private final String name;

        @JsonCreator
        public ObjectWithAnnotatedConstructor(@JsonProperty("name") String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    // for testing deserialization/serialization of parameterized types
    // by carrying type information
    public static class ParameterizedObject<T> {

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
        private T content;

        public T getContent() {
            return content;
        }

        public void setContent(T content) {
            this.content = content;
        }
    }

    // enum with serialization annotations
    public static enum Exclusivity {

        INCLUSIVE("inclusive"), EXCLUSIVE("exclusive");

        private String value;

        private Exclusivity(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return this.value;
        }

        @JsonCreator
        public static Exclusivity create(String val) {
            for (Exclusivity exclusivity : Exclusivity.values()) {
                if (exclusivity.getValue().equalsIgnoreCase(val)) {
                    return exclusivity;
                }
            }
            // default to INCLUSIVE
            return INCLUSIVE;
        }
    }

    // for testing enums
    public static class ObjectWithEnum {
        private Exclusivity exclusivity;

        public void setExclusivity(Exclusivity exclusivity) {
            this.exclusivity = exclusivity;
        }

        public Exclusivity getExclusivity() {
            return exclusivity;
        }
    }

    // for testing the serialization and deserialization of a parameterized list
    // by carrying type information
    public static class ObjectWithParameterizedList<T> {

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
        private List<T> results;

        // results
        public void setResults(List<T> results) {
            this.results = results;
        }

        public List<T> getResults() {
            return results;
        }
    }

    // simple bean with getters and setters
    public static class Person {

        private String firstName;
        private String lastName;

        public Person() {
        }

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getLastName() {
            return lastName;
        }
    }
}
