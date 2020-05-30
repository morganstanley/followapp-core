package com.followapp.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setNodeFactory(JsonNodeFactory.withExactBigDecimals(true))
            .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);

    private JsonUtils() {
    }

    public static String marshall(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Error marshalling Json", exception);
        }
    }

    public static <T> T unMarshall(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException exception) {
            throw new RuntimeException("Error marshalling Json", exception);
        }
    }
}
