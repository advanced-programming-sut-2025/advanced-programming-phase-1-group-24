package org.example.Model.ConfigTemplates;

import java.util.List;
import com.google.gson.Gson;


public class FarmTemplateManager {
    private static List<FarmTemplate> templates;

    public static void loadTemplates() {
        if (templates == null) {
            templates = FarmLoader.loadFarmTemplates();
        }
    }

    public static List<FarmTemplate> getTemplates() {
        return templates;
    }

    public static FarmTemplate getTemplateByType(String type) {
        for (FarmTemplate template : templates) {
            if (template.type.equalsIgnoreCase(type)) {
                return deepCopy(template);
            }
        }
        return null;
    }

    private static FarmTemplate deepCopy(FarmTemplate template) {
        // Use Gson for deep copy
        Gson gson = new Gson();
        String json = gson.toJson(template);
        return gson.fromJson(json, FarmTemplate.class);
    }
}
