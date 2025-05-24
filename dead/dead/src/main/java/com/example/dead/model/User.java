package com.example.dead.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;

@Getter
@Setter
public class User extends Person implements Serializable {
    private LocalDate dateOfBirth;
    private String university;
    private String field;
    private String workPlace;
    private HashSet<String> specialties;
    private HashSet<Long> connectionId;

    public User(long id, String name, String userName, String password, String email, LocalDate dateOfBirth, String university, String field, String workPlace, HashSet<String> specialties, HashSet<Long> connectionId) {
        super(id, name, userName, password, email);
        this.dateOfBirth = dateOfBirth;
        this.university = university;
        this.field = field;
        this.workPlace = workPlace;
        this.specialties = specialties;
        this.connectionId = connectionId;
    }

//    public String toString() {
//        return this.getId() + " * " + this.getName() + " * " + this.getUserName() + " * " + this.getPassword() + " * " + this.getEmail() + " * " + this.getDateOfBirth().toString() + " * " + this.getUniversity() + " * " + this.getField() + " * " + this.getWorkPlace() + " * " + String.join(" , ", this.getSpecialties());
//    }
//
//    public static User toUser(String string) {
//        String[] values = string.split(" \\* ");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate localDate = LocalDate.parse(values[5], formatter);
//
//        return new User(Long.parseLong(values[0]), values[1], values[2], values[3], values[4], localDate, values[6], values[7], values[8], new HashSet<>(Arrays.asList(values[9].split(" , "))), (HashSet<Long>) Arrays.stream(values[10].split(" , "))
//                .map(Long::parseLong)
//                .collect(Collectors.toSet()));
//    }

}
