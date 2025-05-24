package com.example.dead.controller;

import com.example.dead.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileManager {
    private static FileManager instance;

    private FileManager() {
    }

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    public List<User> Read() {
        List<User> users = new ArrayList<>();

        try {
            File[] files = getFiles(DataBaseController.getInstance().getPFile().getPath());

            if (files != null) {
                for (File file : files) {
                    JSONArray jsonArray = extractFile(file);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        User user = userInitializer(jsonObject);
                        users.add(user);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

    private File[] getFiles(String path){
        File directory = new File(path);
        return directory.listFiles((dir, name) -> name.endsWith(".json"));
    }

    private JSONArray extractFile(File file) throws IOException {
        FileReader reader = new FileReader(file);
        StringBuilder jsonData = new StringBuilder();
        int i;

        while ((i = reader.read()) != -1) {
            jsonData.append((char) i);
        }

        reader.close();

        if (jsonData.toString().trim().isEmpty()) {
            throw new IOException("JSON data is empty in file: " + file.getName());
        }

        return new JSONArray(jsonData.toString());
    }

    private User userInitializer(JSONObject jsonObject){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        long id = jsonObject.getLong("id");
        String name = jsonObject.optString("name");
        String university = jsonObject.getString("universityLocation");
        String field = jsonObject.getString("field");
        String workPlace = jsonObject.getString("workplace");
        String username = jsonObject.optString("username");
        String password = jsonObject.optString("password");
        String email = jsonObject.optString("email");

        String birthDateString = jsonObject.getString("dateOfBirth");
        LocalDate dateOfBirth = LocalDate.parse(birthDateString, dateFormatter);

        JSONArray specialtiesArray = jsonObject.getJSONArray("specialties");
        HashSet<String> specialties = new HashSet<>();
        for (int j = 0; j < specialtiesArray.length(); j++) {
            specialties.add(specialtiesArray.getString(j));
        }

        JSONArray connectionArray = jsonObject.getJSONArray("connectionId");
        HashSet<Long> connectionId = new HashSet<>();
        for (int j = 0; j < connectionArray.length(); j++) {
            connectionId.add(Long.valueOf(connectionArray.getString(j)));
        }

        return new User(id, name, username, password, email, dateOfBirth, university, field, workPlace, specialties, connectionId);
    }

    public void writeUsersToFile(List<User> users) {
        deleteAllFile();
        File newFile = new File(DataBaseController.getInstance().getPFile().getPath() + "/users.json");

        try (FileWriter fileWriter = new FileWriter(newFile)) {
            JSONArray jsonArray = new JSONArray();

            for (User user : users) {
                JSONObject jsonObject = userToJson(user);
                jsonArray.put(jsonObject);
            }

            fileWriter.write(jsonArray.toString(4));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteAllFile(){
        File directory = new File(DataBaseController.getInstance().getPFile().getPath());
        if (directory.exists() && directory.isDirectory()) {

            for (File file : directory.listFiles()) {
                if (file.isFile())
                    file.delete();
            }
        }
    }

    private static JSONObject userToJson(User user) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", String.valueOf(user.getId()));
        jsonObject.put("name", user.getName());
        jsonObject.put("universityLocation", user.getUniversity());
        jsonObject.put("field", user.getField());
        jsonObject.put("workplace", user.getWorkPlace());
        jsonObject.put("username", user.getUserName());
        jsonObject.put("password", user.getPassword());
        jsonObject.put("email", user.getEmail());
        jsonObject.put("dateOfBirth", user.getDateOfBirth().format(dateFormatter));

        JSONArray specialtiesArray = new JSONArray(user.getSpecialties());
        jsonObject.put("specialties", specialtiesArray);

        JSONArray connectionArray = new JSONArray();
        for (Long id : user.getConnectionId()) {
            connectionArray.put(String.valueOf(id));
        }
        jsonObject.put("connectionId", connectionArray);

        return jsonObject;
    }

}
