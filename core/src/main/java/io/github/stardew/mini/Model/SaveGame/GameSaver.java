package io.github.stardew.mini.Model.SaveGame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.*;
import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Game;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.stardew.mini.Model.Growables.Growable;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.NPCManagement.NPC;
import io.github.stardew.mini.Model.NPCManagement.NPCMission;
import io.github.stardew.mini.Model.Things.Food;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.User;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GameSaver {

    public static ObjectMapper createCustomObjectMapper() {
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
        module.addKeySerializer(NPC.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(NPC.class, new GenericKeyDeserializer<>());
        module.addKeySerializer(NPCMission.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(NPCMission.class, new GenericKeyDeserializer<>());
        mapper.registerModule(module);

        return mapper;
    }

/// ///////////////// save with json
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

    /// ///////////////////save with zip
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
    public static String serializeAndCompressGame(Game game) throws IOException {
        ObjectMapper mapper = createCustomObjectMapper();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(byteStream);
             OutputStreamWriter writer = new OutputStreamWriter(gzip, StandardCharsets.UTF_8)) {
            mapper.writeValue(writer, game);
        }
        byte[] compressed = byteStream.toByteArray();
        return Base64.getEncoder().encodeToString(compressed);
    }
    public static <T> T convertObject(Object rawObj, Class<T> clazz) {
        return GameSaver.createCustomObjectMapper().convertValue(rawObj, clazz);
    }
    public static Game loadSingleGameFromCompressedBytes(byte[] compressedBytes) throws IOException {
        ObjectMapper mapper = createCustomObjectMapper();
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(compressedBytes);
             GZIPInputStream gzipStream = new GZIPInputStream(byteStream);
             InputStreamReader reader = new InputStreamReader(gzipStream, StandardCharsets.UTF_8)) {
            return mapper.readValue(reader, Game.class);
        }
    }
//    RMap<String, String> gameMap = redissonClient.getMap("savedGames");
//    String compressed = GameSaver.serializeAndCompressGame(game);
//gameMap.put(game.getId(), compressed); // Save
//
//    String data = gameMap.get(gameId); // Load
//    byte[] decoded = Base64.getDecoder().decode(data);
// Deserialize as usual

}
