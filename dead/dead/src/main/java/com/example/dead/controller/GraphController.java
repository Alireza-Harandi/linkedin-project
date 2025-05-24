package com.example.dead.controller;

import com.example.dead.model.Attributes;
import com.example.dead.model.Pair;
import com.example.dead.model.User;

import java.util.*;

public class GraphController {
    private static GraphController instance;

    private GraphController() {
    }

    public static GraphController getInstance() {
        if (instance == null) {
            instance = new GraphController();
        }
        return instance;
    }

    public void makeGraph() {
        List<User> users = new ArrayList<>(DataBaseController.getInstance().getUsers());

        for (User user : users) {
            DataBaseController.getInstance().getGraph().insertVertex(user);
        }

        for (User user : users) {
            for (long connection : user.getConnectionId()) {
                User temp = DataBaseController.getInstance().getId(connection);
                DataBaseController.getInstance().getGraph().insertEdge(user, temp, DataBaseController.getInstance().getNextIndex());
            }
        }
    }

    public void addUser(User user) {
        DataBaseController.getInstance().getGraph().insertVertex(user);
    }

    public void removeUser(User user) {
        DataBaseController.getInstance().getGraph().removeVertex(user);
    }

    public void addConnection(User user1, User user2) {
        DataBaseController.getInstance().getGraph().insertEdge(user1, user2, DataBaseController.getInstance().getNextIndex());
    }

    public void removeConnection(User user1, User user2) {
        DataBaseController.getInstance().getGraph().removeEdgeBetween(user1, user2);
    }

    public List<User> generalSuggestion(User user) {
        if (user.getConnectionId().isEmpty()) {
            User user1 = DataBaseController.getInstance().getUsers().getFirst();
            return DataBaseController.getInstance().getGraph().bfs(user1).stream().map(Pair::getKey).toList();
        } else {
            List<Pair<User, Integer>> suggestions = DataBaseController.getInstance().getGraph().bfs(user);
            Map<User, Integer> map = new HashMap<>();

            for (Pair<User, Integer> pair : suggestions) {
                int value = pair.getValue() * 10 * Attributes.CONNECTION.getValue();

                if (!pair.getKey().getUniversity().equalsIgnoreCase(user.getUniversity()))
                    value += 20 * Attributes.UNIVERSITY.getValue();

                if (!pair.getKey().getField().equalsIgnoreCase(user.getField()))
                    value += 20 * Attributes.FIELD.getValue();

                if (!pair.getKey().getWorkPlace().equalsIgnoreCase(user.getWorkPlace()))
                    value += 20 * Attributes.WORKPLACE.getValue();

                value += 20 * Attributes.SPECIALTIES.getValue();

                int index = 0;
                for (String speciality : pair.getKey().getSpecialties()) {
                    if (user.getSpecialties().contains(speciality)) {
                        value -= 4 * Attributes.SPECIALTIES.getValue();
                        index++;
                    }

                    if (index == 5)
                        break;
                }

                map.put(pair.getKey(), value);
            }

            return map.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .toList();
        }
    }

    public List<User> customizeSuggestion(User user, String input) {
        List<Integer> values = Arrays.stream(input.split(" "))
                .map(Integer::parseInt)
                .toList();

        //uni, filed, workplace, specialties, connection
        Attributes.updateAllValues(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4));

        return generalSuggestion(user);
    }

    public List<User> uniSuggestion(User user) {
        List<User> list = new ArrayList<>();
        List<User> users = DataBaseController.getInstance().getUniversity(user.getUniversity());
        for (User user1 : users) {
            if (list.size() == 20)
                break;
            if (user != user1)
                list.add(user1);
        }

        return list;
    }

    public List<User> workSuggestion(User user) {
        List<User> list = new ArrayList<>();
        List<User> users = DataBaseController.getInstance().getWorkPlace(user.getWorkPlace());
        for (User user1 : users) {
            if (list.size() == 20)
                break;
            if (user != user1)
                list.add(user1);
        }

        return list;
    }

    public List<User> filedSuggestion(User user) {
        List<User> list = new ArrayList<>();
        List<User> users = DataBaseController.getInstance().getField(user.getField());
        for (User user1 : users) {
            if (list.size() == 20)
                break;
            if (user != user1)
                list.add(user1);
        }

        return list;
    }

    public void addToDegreeMap(List<User> users) {
        for (User user : users) {
            updateDegreeMap(user);
        }
    }

    private void updateDegreeMap(User user) {
        int oldDegree = DataBaseController.getInstance().getGraph().getOrDefault(user).size();

        if (DataBaseController.getInstance().getDegreeMap().containsKey(oldDegree)) {
            DataBaseController.getInstance().getDegreeMap().get(oldDegree).remove(user);
            if (DataBaseController.getInstance().getDegreeMap().get(oldDegree).isEmpty()) {
                DataBaseController.getInstance().getDegreeMap().remove(oldDegree);
            }
        }

        int newDegree = DataBaseController.getInstance().getGraph().getOrDefault(user).size();
        DataBaseController.getInstance().getDegreeMap().computeIfAbsent(newDegree, k -> new HashSet<>()).add(user);
    }


    public List<User> getTop4InfluencersTreeMap(User user1) {
        List<User> result = new ArrayList<>();
        for (Set<User> users : DataBaseController.getInstance().getDegreeMap().values()) {
            for (User user : users) {
                if (result.size() < 4) {
                    if (user != user1)
                        result.add(user);
                } else
                    return result;
            }
        }

        return result;
    }
}
