package io.github.stardew.mini.common.Model.ConfigTemplates;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FarmLoader {

    public static List<FarmTemplate> loadFarmTemplates() {
        File file = new File("assets/farms.json");

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
