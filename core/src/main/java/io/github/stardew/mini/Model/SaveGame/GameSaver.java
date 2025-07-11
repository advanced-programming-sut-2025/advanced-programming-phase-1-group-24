package io.github.stardew.mini.Model.SaveGame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;

import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Game; // change based on your path

import java.nio.charset.StandardCharsets;
import java.util.List;

//public class GameSaver {
//
//    public static void saveGames(List<Game> games, String filePath) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.findAndRegisterModules(); // support for Java 8 types
//        mapper.enable(SerializationFeature.INDENT_OUTPUT); // pretty JSON
//        mapper.writeValue(new File(filePath), games);
//    }
//
//    public static List<Game> loadGames(String filePath) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.findAndRegisterModules();
//        return mapper.readValue(new File(filePath),
//            mapper.getTypeFactory().constructCollectionType(List.class, Game.class));
//    }
//}

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Growables.Growable;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.Things.Food;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.User;

import java.io.File;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

//
//public class GameSaver {
//
//    private static ObjectMapper createCustomObjectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//
//        // Register support for Java 8 time, optional, etc.
//        mapper.findAndRegisterModules();
//
//        // Optional: Pretty-print JSON
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//        // Register custom (de)serializers for Item keys in maps
//        SimpleModule module = new SimpleModule();
//
//       // Register complex map keys here
//        module.addKeySerializer(Item.class, new GenericKeySerializer<>());
//        module.addKeyDeserializer(Item.class, new GenericKeyDeserializer<>());
//        module.addKeySerializer(User.class, new GenericKeySerializer<>());
//        module.addKeyDeserializer(User.class, new GenericKeyDeserializer<>());
//        module.addKeySerializer(Tile.class, new GenericKeySerializer<>());
//        module.addKeyDeserializer(Tile.class, new GenericKeyDeserializer<>());
//        module.addKeySerializer(Food.class, new GenericKeySerializer<>());
//        module.addKeyDeserializer(Food.class, new GenericKeyDeserializer<>());
//        module.addKeySerializer(Growable.class, new GenericKeySerializer<>());
//        module.addKeyDeserializer(Growable.class, new GenericKeyDeserializer<>());
//
//        // Add more as needed...
//
//        mapper.registerModule(module);
//
//        return mapper;
//    }
//
//    public static void saveGames(List<Game> games, String filePath) throws Exception {
//        ObjectMapper mapper = createCustomObjectMapper();
//        mapper.writeValue(new File(filePath), games);
//    }
//
//    public static List<Game> loadGames(String filePath) throws Exception {
//        ObjectMapper mapper = createCustomObjectMapper();
//        return mapper.readValue(
//            new File(filePath),
//            mapper.getTypeFactory().constructCollectionType(List.class, Game.class)
//        );
//    }
//}
public class GameSaver {

    private static ObjectMapper createCustomObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.findAndRegisterModules();

        // Optional: Pretty-print JSON
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Register custom (de)serializers for Item keys in maps
        SimpleModule module = new SimpleModule();

        // Register complex map keys here
        module.addKeySerializer(Item.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(Item.class, new GenericKeyDeserializer<>());
        module.addKeySerializer(User.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(User.class, new GenericKeyDeserializer<>());
        module.addKeySerializer(Tile.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(Tile.class, new GenericKeyDeserializer<>());
        module.addKeySerializer(Food.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(Food.class, new GenericKeyDeserializer<>());
        module.addKeySerializer(Growable.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(Growable.class, new GenericKeyDeserializer<>());
        module.addKeySerializer(Animal.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(Animal.class, new GenericKeyDeserializer<>());


        mapper.registerModule(module);

        return mapper;
    }
/// //////////// save with json
//    public static void saveGames(List<Game> games, String filePath) throws Exception {
//        ObjectMapper mapper = createCustomObjectMapper();
//        mapper.writeValue(new File(filePath), games);
//    }
//
//    public static List<Game> loadGames(String filePath) throws Exception {
//        ObjectMapper mapper = createCustomObjectMapper();
//        return mapper.readValue(
//            new File(filePath),
//            mapper.getTypeFactory().constructCollectionType(List.class, Game.class)
//        );
//    }
    /// ///////////save with zip
    public static void saveGames(List<Game> games, String filePath) throws Exception {
        ObjectMapper mapper = createCustomObjectMapper();
        try (OutputStream fileStream = new FileOutputStream(filePath);
             OutputStream gzipStream = new GZIPOutputStream(fileStream);
             OutputStreamWriter writer = new OutputStreamWriter(gzipStream, StandardCharsets.UTF_8)) {
            mapper.writeValue(writer, games);
        }
    }
    public static List<Game> loadGames(String filePath) throws Exception {
        ObjectMapper mapper = createCustomObjectMapper();
        try (InputStream fileStream = new FileInputStream(filePath);
             InputStream gzipStream = new GZIPInputStream(fileStream);
             InputStreamReader reader = new InputStreamReader(gzipStream, StandardCharsets.UTF_8)) {
            return mapper.readValue(reader,
                mapper.getTypeFactory().constructCollectionType(List.class, Game.class));
        }
    }


}
