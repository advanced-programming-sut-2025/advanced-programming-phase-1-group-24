package org.example.Model.ConfigTemplates;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.Model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FarmLoader {

    public static List<FarmTemplate> loadFarmTemplates() {

        File file = new File("src/main/resources/farms.json");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type farmListType = new TypeToken<ArrayList<FarmTemplate>>() {}.getType();
            return gson.fromJson(reader, farmListType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
