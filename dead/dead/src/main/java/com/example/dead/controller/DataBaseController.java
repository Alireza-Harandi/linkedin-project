package com.example.dead.controller;

import com.example.dead.model.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class DataBaseController {
    private static DataBaseController instance;

    private DataBaseController() {
        graph = new UnGraph<>();
        index = 0;
        trieTree = new TrieTree<>();
        usernames = new HashSet<>();
        verificationCodes = new HashMap<>();
        degreeMap = new TreeMap<>(Collections.reverseOrder());
        maxId = 0;
    }

    public static DataBaseController getInstance() {
        if (instance == null) {
            instance = new DataBaseController();
        }
        return instance;
    }

    @Getter
    @Setter
    private UnGraph<User, Long> graph;
    private long index;
    @Getter
    @Setter
    private TrieTree<User> trieTree;
    @Getter
    @Setter
    private HashSet<String> usernames;
    @Getter
    @Setter
    private List<User> users;
    @Getter
    @Setter
    private Map<String, String> verificationCodes;
    @Getter
    @Setter
    private TreeMap<Integer, HashSet<User>> degreeMap;
    @Getter
    @Setter
    private PFile pFile;

    private long maxId;

    private final BPlusTree<Long, User> idIndex = new BPlusTree<>(-1);
    private final BPlusTree<String, User> usernameIndex = new BPlusTree<>(-1);
    private final BPlusTree<String, User> universityIndex = new BPlusTree<>(-1);
    private final BPlusTree<String, User> fieldIndex = new BPlusTree<>(-1);
    private final BPlusTree<String, User> workPlaceIndex = new BPlusTree<>(-1);

    public void preProcess(String path) {
        pFile = new PFile(path);
        users = FileManager.getInstance().Read();

        for (User user : users) {
            trieTree.insert(user.getUserName(), user);
            usernames.add(user.getUserName());
            maxId = Math.max(user.getId(), maxId);

            addToTable(user);
        }

        GraphController.getInstance().makeGraph();
        GraphController.getInstance().addToDegreeMap(users);
    }


    public User getId(long id) {
        return idIndex.search(id);
    }

    public User getUsername(String username) {
        return usernameIndex.search(username);
    }

    public List<User> getUniversity(String university) {
        return universityIndex.searchAll(university);
    }

    public List<User> getField(String field) {
        return fieldIndex.searchAll(field);
    }

    public List<User> getWorkPlace(String workPlace) {
        return workPlaceIndex.searchAll(workPlace);
    }

    public void addToTable(User user) {
        idIndex.insert(user.getId(), user);
        usernameIndex.insert(user.getUserName(), user);
        universityIndex.insert(user.getUniversity(), user);
        fieldIndex.insert(user.getField(), user);
        workPlaceIndex.insert(user.getWorkPlace(), user);
    }

    public long getNextIndex() {
        return index++;
    }

    public long getNextId() {
        return maxId+1;
    }

    public void removeFromTable(User user) {
        idIndex.delete(user.getId());
        usernameIndex.delete(user.getUserName());
        universityIndex.delete(user.getUniversity());
        fieldIndex.delete(user.getField());
        workPlaceIndex.delete(user.getWorkPlace());
    }
}
