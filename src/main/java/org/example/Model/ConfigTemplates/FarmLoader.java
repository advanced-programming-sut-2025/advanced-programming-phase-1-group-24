package org.example.Model.ConfigTemplates;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class FarmLoader {

    public static List<FarmTemplate> loadFarmTemplates() {
        try {
            InputStream is = FarmLoader.class.getClassLoader().getResourceAsStream("farm.json");
            if (is == null) {
                throw new RuntimeException("farm.json not found in resources!");
            }

            InputStreamReader reader = new InputStreamReader(is);
            Type listType = new TypeToken<List<FarmTemplate>>(){}.getType();
            return new Gson().fromJson(reader, listType);
        } catch (Exception e) {
            throw new RuntimeException("Error loading farm templates", e);
        }
    }
}
