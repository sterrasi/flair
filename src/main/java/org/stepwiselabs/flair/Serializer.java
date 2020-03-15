package org.stepwiselabs.flair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.stepwiselabs.flair.exceptions.SerializationException;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * General use parameterized serialization interface.
 *
 * @param <T>
 */
public abstract class Serializer<T> {

    /**
     * Deserialize the given string {@code data} into the parameterized type associated with
     * this {@code Serializer}.
     *
     * @param data A {@link java.lang.String string} containing the serialized Object.
     * @return An {@link java.lang.Object object} of the parameterized type associated
     * with this {@code Serializer}.
     * @throws SerializationException
     */
    public abstract T deserialize(final String data) throws SerializationException;

    /**
     * Deserialize the given {@code data} stream into the parameterized type associated with
     * this {@code Serializer}.
     *
     * @param data An {@link java.io.InputStream input stream} of object data.
     * @return An {@link java.lang.Object object} of the parameterized type associated
     * with this {@code Serializer}.
     * @throws SerializationException
     */
    public abstract T deserialize(InputStream data) throws SerializationException;

    /**
     * Deserialize the given string {@code arrayData} into a {@link java.util.List list}
     * of Objects of the parameterized type associated with this {@code Serializer}.
     *
     * @param arrayData A {@link java.lang.String string} containing a serialized list of Objects.
     * @return A list of objects of the parameterized type associated with this {@code Serializer}.
     * @throws SerializationException
     */
    public abstract List<T> deserializeList(final String arrayData)
            throws SerializationException;

    /**
     * Deserialize the given {@code arrayData} stream into a {@link java.util.List list}
     * of Objects of the parameterized type associated with this {@code Serializer}.
     *
     * @param arrayData
     * @return A list of Objects of the parameterized type associated with this {@code Serializer}.
     * @throws SerializationException
     */
    public abstract List<T> deserializeList(final InputStream arrayData)
            throws SerializationException;

    /**
     * Serializes the given {@code object}.
     *
     * @param object
     * @return The serialized form of {@code object}, as a {@link java.lang.String string}
     * @throws SerializationException
     */
    public abstract String serialize(final T object) throws SerializationException;

    /**
     * Serializes the given {@code object}.
     *
     * @param object
     * @return the serialized form of {@code object}, as a {@code byte[]}
     * @throws SerializationException
     */
    public abstract byte[] serializeAsBytes(final T object) throws SerializationException;


    /**
     * Serializes the given {@code objectList}.
     *
     * @param objectList
     * @return the serialized form of {@code objectList}, as a {@link java.lang.String string}
     * @throws SerializationException
     */
    public abstract String serializeList(final List<T> objectList) throws SerializationException;


    /**
     * Creates a new {@code Serializer} for the given {@code objectClass}.
     *
     * @param mapper      ObjectMapper to use for serialization
     * @param objectClass Class for parameterized type that the {@code Serializer} is associated with.
     * @return a new {@code Serializer}
     */
    public static <T> Serializer<T> create(final ObjectMapper mapper, final Class<T> objectClass) {
        return new JacksonSerializer<>(mapper, objectClass);
    }

    /**
     * Creates a new {@code Serializer} for the given {@code objectClass}.
     *
     * @param mapper      ObjectMapper to use for serialization
     * @param objectClass Class for parameterized type that the {@code Serializer} is associated with.
     * @param pretty      Options flag to enable writing pretty output.
     * @return a new {@code Serializer}
     */
    public static <T> Serializer<T> create(final ObjectMapper mapper, final Class<T> objectClass, boolean pretty) {
        return new JacksonSerializer<>(mapper, objectClass, pretty);
    }

    /**
     * A {@code Serializer} backed by the Jackson JSON engine.
     *
     * @param <T>
     */
    private static class JacksonSerializer<T> extends Serializer<T> {

        private final Class<T> mapperClazz;
        private final ObjectMapper mapper;
        private final boolean pretty;

        JacksonSerializer(final ObjectMapper mapper, final Class<T> mapperClazz) {
            this(mapper, mapperClazz, false);
        }

        JacksonSerializer(final ObjectMapper mapper, final Class<T> mapperClazz, boolean pretty) {
            this.mapper = mapper;
            this.mapperClazz = mapperClazz;
            this.pretty = pretty;
        }

        @Override
        public T deserialize(final String data) throws SerializationException {
            try {
                return mapper.readValue(data, mapperClazz);
            } catch (Exception e) {
                throw new SerializationException(e,
                        "Caught '%s' while trying to deserialize value '%s' into a '%s'. Error = %s",
                        e.getClass().getSimpleName(), data,
                        mapperClazz.getSimpleName(), e.getMessage());
            }
        }

        @Override
        public T deserialize(final InputStream data) throws SerializationException {
            try {
                return mapper.readValue(data, mapperClazz);
            } catch (Exception e) {
                throw new SerializationException(e,
                        "Caught '%s' while trying to deserialize stream into a '%s'. Error = %s",
                        e.getClass().getSimpleName(),
                        mapperClazz.getSimpleName(), e.getMessage());
            }
        }

        @Override
        public List<T> deserializeList(final InputStream arrayData)
                throws SerializationException {
            try {
                JsonNode arrayNode = mapper.readTree(arrayData);
                CollectionType typeRef = mapper.getTypeFactory()
                        .constructCollectionType(List.class, mapperClazz);
                return mapper.readValue(arrayNode.traverse(), typeRef);
            } catch (Exception e) {
                throw new SerializationException(e,
                        "Caught '%s' while trying to deserialize array stream into a '%s'. Error = %s",
                        e.getClass().getSimpleName(),
                        mapperClazz.getSimpleName(), e.getMessage());
            }
        }

        @Override
        public List<T> deserializeList(final String arrayData) throws SerializationException {
            try {
                JsonNode arrayNode = mapper.readTree(arrayData);
                CollectionType typeRef = mapper.getTypeFactory()
                        .constructCollectionType(List.class, mapperClazz);
                return mapper.readValue(arrayNode.traverse(), typeRef);
            } catch (Exception e) {
                throw new SerializationException(e,
                        "Caught '%s' while trying to deserialize array '%s' into a '%s'. Error = %s",
                        e.getClass().getSimpleName(), arrayData,
                        mapperClazz.getSimpleName(), e.getMessage());
            }
        }

        @Override
        public String serializeList(final List<T> objectList) throws SerializationException {
            try {
                return mapper.writerFor(List.class).writeValueAsString(objectList);
            } catch (JsonProcessingException e) {
                throw new SerializationException(e,
                        "Caught '%s' while trying to serialize list '%s' into a '%s'.  Error = %s",
                        e.getClass().getSimpleName(), Arrays.toString(objectList.toArray()),
                        mapperClazz.getSimpleName(), e.getMessage());
            }
        }

        @Override
        public String serialize(final T obj) throws SerializationException {
            try {
                if (pretty) {
                    return mapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(obj);
                } else {
                    return mapper.writeValueAsString(obj);
                }
            } catch (Exception e) {
                throw new SerializationException(
                        "Caught '%s' while trying to serialize object '%s'. "
                                + "Error = %s", e,
                        e.getClass().getSimpleName(), obj, e.getMessage());
            }
        }

        @Override
        public byte[] serializeAsBytes(final T obj) throws SerializationException {
            try {
                if (pretty) {
                    return mapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsBytes(obj);
                } else {
                    return mapper.writeValueAsBytes(obj);
                }
            } catch (Exception e) {
                throw new SerializationException(
                        "Caught '%s' while trying to serialize object '%s'. "
                                + "Error = %s", e,
                        e.getClass().getSimpleName(), obj, e.getMessage());
            }
        }
    }
}
