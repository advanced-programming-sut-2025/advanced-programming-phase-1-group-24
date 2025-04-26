package org.example.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserDatabase {
    private static final String FILE_PATH = "data/users.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static ArrayList<User> loadUsers() {
        File file = new File(FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type userListType = new TypeToken<ArrayList<User>>() {}.getType();
            return gson.fromJson(reader, userListType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }


    public static void saveUsers(ArrayList<User> users) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // create 'data/' folder if it doesn't exist

            try (Writer writer = new FileWriter(file)) {
                if (users == null) users = new ArrayList<>();
                gson.toJson(users, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

