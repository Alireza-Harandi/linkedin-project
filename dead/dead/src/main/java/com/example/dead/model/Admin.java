package com.example.dead.model;

import java.io.Serializable;

public class Admin extends Person implements Serializable {

    public Admin(long id, String name, String userName, String password, String email) {
        super(id, name, userName, password, email);
    }
}
