package com.example.windows.gymapp.model;

import java.io.Serializable;

/**
 * Created by windows on 08/08/2019.
 */

public class Section implements Serializable{

    private String name;
    private String id;
    private String id_creator;


    public Section(String name, String id, String id_creator) {
        this.name = name;
        this.id = id;
        this.id_creator = id_creator;

    }

    public Section() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_creator() {
        return id_creator;
    }

    public void setId_creator(String id_creator) {
        this.id_creator = id_creator;
    }
}
