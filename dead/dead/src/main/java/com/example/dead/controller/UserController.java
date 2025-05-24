package com.example.dead.controller;


import com.example.dead.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class UserController {
    private static UserController instance;

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public User getProfile(String username) {
        return DataBaseController.getInstance().getUsername(username);
    }

    public List<Object> getAllProfiles() {
        List<Object> list = new ArrayList<>();

        list.add("profiles");
        List<User> users = new ArrayList<>(DataBaseController.getInstance().getUsers());
        list.add(users);

        return list;
    }

    public List<User> search(String input) {
        return DataBaseController.getInstance().getTrieTree().searchTop5(input);
    }

    public int purification(User user) {
        int result = 0;
        int length = user.getConnectionId().size();

        for (long id : user.getConnectionId()) {
            User temp = DataBaseController.getInstance().getId(id);
            result += helpPurification(user, temp);
        }

        if (length == 0) {
            return 0;
        }
        return result / length;
    }

    private int helpPurification(User user1, User user2) {
        int result = 0;

        if (Objects.equals(user1.getWorkPlace(), user2.getWorkPlace())) result += 25;
        if (Objects.equals(user1.getField(), user2.getField())) result += 25;
        if (Objects.equals(user1.getUniversity(), user2.getUniversity())) result += 25;
        result += calculateSimilarity(user1.getSpecialties(), user2.getSpecialties());

        return result;
    }

    private int calculateSimilarity(HashSet<String> set1, HashSet<String> set2) {
        if (set1.isEmpty() && set2.isEmpty()) return 25;
        int commonElements = 0;

        for (String item : set1) {
            if (set2.contains(item)) {
                commonElements++;
            }
        }

        int maxSize = Math.max(set1.size(), set2.size());
        return (int) Math.round(((double) commonElements / maxSize) * 25);
    }

    public List<Object> connect(String username1, String username2) {
        User user1 = DataBaseController.getInstance().getUsername(username1);
        User user2 = DataBaseController.getInstance().getUsername(username2);
        List<User> users = new ArrayList<>() {{
            add(user1);
            add(user2);
        }};

        user1.getConnectionId().add(user2.getId());
        user2.getConnectionId().add(user1.getId());

        GraphController.getInstance().addConnection(user1, user2);
        GraphController.getInstance().addToDegreeMap(users);

        return new ArrayList<>(List.of("connected"));
    }

    public List<Object> disConnect(String username1, String username2) {
        User user1 = DataBaseController.getInstance().getUsername(username1);
        User user2 = DataBaseController.getInstance().getUsername(username2);
        List<User> users = new ArrayList<>() {{
            add(user1);
            add(user2);
        }};

        user1.getConnectionId().remove(user2.getId());
        user2.getConnectionId().remove(user1.getId());

        GraphController.getInstance().removeConnection(user1, user2);
        GraphController.getInstance().addToDegreeMap(users);

        return new ArrayList<>(List.of("disconnected"));
    }

    public List<Object> getAllConnections(User user) {
        List<Object> list = new ArrayList<>();
        list.add("connections");

        List<User> users = new ArrayList<>();
        for (long id : user.getConnectionId()) {
            User temp = DataBaseController.getInstance().getId(id);
            users.add(temp);
        }

        list.add(users);
        return list;
    }

    public List<Object> getMainScene(User user) {
        List<Object> list = new ArrayList<>();
        list.add("getMainScene");
        list.add(GraphController.getInstance().getTop4InfluencersTreeMap(user));
        list.add(GraphController.getInstance().generalSuggestion(user));
        list.add(GraphController.getInstance().uniSuggestion(user));
        list.add(GraphController.getInstance().workSuggestion(user));
        list.add(GraphController.getInstance().filedSuggestion(user));

        return list;
    }

    public void deleteAccount(User user) {
        GraphController.getInstance().removeUser(user);
        DataBaseController.getInstance().getTrieTree().delete(user.getUserName());
        DataBaseController.getInstance().getUsernames().remove(user.getUserName());
        DataBaseController.getInstance().getUsers().remove(user);

        int oldDegree = DataBaseController.getInstance().getGraph().getOrDefault(user).size();
        if (DataBaseController.getInstance().getDegreeMap().containsKey(oldDegree)) {
            DataBaseController.getInstance().getDegreeMap().get(oldDegree).remove(user);
            if (DataBaseController.getInstance().getDegreeMap().get(oldDegree).isEmpty()) {
                DataBaseController.getInstance().getDegreeMap().remove(oldDegree);
            }
        }

        for (long id : user.getConnectionId()) {
            User temp = DataBaseController.getInstance().getId(id);
            temp.getConnectionId().remove(user.getId());
        }

        DataBaseController.getInstance().removeFromTable(user);
    }
}
