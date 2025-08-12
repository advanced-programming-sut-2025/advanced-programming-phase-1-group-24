package io.github.stardew.mini.common.Model.SaveGame;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class GenericKeyDeserializer<T> extends KeyDeserializer {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        int sep = key.indexOf('|');
        if (sep == -1) {
            throw new IOException("Malformed key: " + key);
        }

        String className = key.substring(0, sep);
        String json = key.substring(sep + 1);

        try {
            Class<?> clazz = Class.forName(className);
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new IOException("Failed to deserialize key: " + key, e);
        }
    }
}
