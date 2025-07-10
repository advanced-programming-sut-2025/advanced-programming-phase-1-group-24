package io.github.stardew.mini.Model.SaveGame;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.stardew.mini.Model.Things.Item;

import java.io.IOException;
//
//public class ItemKeyDeserializer extends KeyDeserializer {
//    @Override
//    public Item deserializeKey(String key, DeserializationContext ctxt) throws IOException {
//        int sep = key.indexOf('|');
//        if (sep == -1) throw new IOException("Malformed key: " + key);
//
//        String className = key.substring(0, sep);
//        String json = key.substring(sep + 1);
//
//        try {
//            Class<?> clazz = Class.forName(className);
//            ObjectMapper mapper = new ObjectMapper();
//            return (Item) mapper.readValue(json, clazz);
//        } catch (ClassNotFoundException e) {
//            throw new IOException("Unknown class: " + className, e);
//        }
//    }
//}
public class ItemKeyDeserializer extends KeyDeserializer {
    @Override
    public Item deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        int sep = key.indexOf('|');
        if (sep == -1) throw new IOException("Malformed key: " + key);

        String className = key.substring(0, sep);
        String json = key.substring(sep + 1);

        try {
            Class<?> clazz = Class.forName(className);
            ObjectMapper mapper = new ObjectMapper();
            return (Item) mapper.readValue(json, clazz);
        } catch (ClassNotFoundException e) {
            throw new IOException("Unknown class: " + className, e);
        }
    }
}
