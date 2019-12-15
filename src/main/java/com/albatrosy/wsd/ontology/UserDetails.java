package com.albatrosy.wsd.ontology;

import jade.content.Concept;

public class UserDetails implements Concept {

    private String name;

    public UserDetails() {
        this.name = "";
    }

    public UserDetails(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
