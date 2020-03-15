package org.stepwiselabs.flair;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializerFactory {

    public final ObjectMapper mapper;

    public SerializerFactory(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> Serializer<T> createForClass(Class<T> clazz) {
        return Serializer.create(mapper, clazz);
    }

    public <T> Serializer<T> createForClass(Class<T> clazz, boolean pretty) {
        return Serializer.create(mapper, clazz, pretty);
    }
}
