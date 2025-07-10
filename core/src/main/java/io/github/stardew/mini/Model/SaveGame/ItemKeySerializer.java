package io.github.stardew.mini.Model.SaveGame;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.stardew.mini.Model.Things.Item;

import java.io.IOException;

//public class ItemKeySerializer extends JsonSerializer<Item> {
//    @Override
//    public void serialize(Item item, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//        // You can customize what uniquely identifies the item
//        gen.writeFieldName(item.getClass().getName() + "|" + new ObjectMapper().writeValueAsString(item));
//    }
//}

public class ItemKeySerializer extends JsonSerializer<Item> {
    @Override
    public void serialize(Item item, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String itemJson = new ObjectMapper().writeValueAsString(item);
        gen.writeFieldName(item.getClass().getName() + "|" + itemJson);
    }
}

