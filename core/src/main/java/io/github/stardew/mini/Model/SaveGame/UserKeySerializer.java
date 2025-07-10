package io.github.stardew.mini.Model.SaveGame;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.stardew.mini.Model.User;

import java.io.IOException;

public class UserKeySerializer extends JsonSerializer<User> {
    @Override
    public void serialize(User user, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String userJson = new ObjectMapper().writeValueAsString(user);
        gen.writeFieldName(user.getClass().getName() + "|" + userJson);
    }
}
