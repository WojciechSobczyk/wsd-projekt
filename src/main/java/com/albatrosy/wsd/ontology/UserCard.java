package com.albatrosy.wsd.ontology;

import jade.content.Concept;

public class UserCard implements Concept {

    private String description;

    public UserCard() {
        this.description = "";
    }

    public UserCard(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
