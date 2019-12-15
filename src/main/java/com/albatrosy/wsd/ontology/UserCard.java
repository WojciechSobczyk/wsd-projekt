package com.albatrosy.wsd.ontology;

import jade.content.Concept;

public class UserCard implements Concept {

    private Long x;
    private Long y;
    private String name;
    private String description;

    public UserCard() {
        this.description = "";
    }

    public UserCard(Long x, Long y, String name, String description) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
