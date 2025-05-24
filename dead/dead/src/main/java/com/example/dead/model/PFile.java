package com.example.dead.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PFile {

    private String path;

    public PFile(String path) {
        this.path = path;
    }
}
