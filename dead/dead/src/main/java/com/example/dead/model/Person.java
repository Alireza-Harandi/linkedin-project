package com.example.dead.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class Person implements Serializable {
    private long id;
    private String name;
    private String userName;
    private String password;
    private String email;

    public Person(long id, String name, String userName, String password, String email) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.email = email;
    }
}
