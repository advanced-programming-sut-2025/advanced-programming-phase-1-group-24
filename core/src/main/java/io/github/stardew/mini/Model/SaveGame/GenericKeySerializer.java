package io.github.stardew.mini.Model.SaveGame;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericKeySerializer<T> extends JsonSerializer<T> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String json = mapper.writeValueAsString(value);
        String className = value.getClass().getName();
        gen.writeFieldName(className + "|" + json);
    }
}
