package io.github.stardew.mini.Model.SaveGame;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import io.github.stardew.mini.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class UserKeyDeserializer extends KeyDeserializer {
    @Override
    public User deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        int sep = key.indexOf('|');
        if (sep == -1) throw new IOException("Malformed key: " + key);

        String className = key.substring(0, sep);
        String json = key.substring(sep + 1);

        try {
            Class<?> clazz = Class.forName(className);
            ObjectMapper mapper = new ObjectMapper();
            return (User) mapper.readValue(json, clazz);
        } catch (ClassNotFoundException e) {
            throw new IOException("Unknown class: " + className, e);
        }
    }
}

