package com.albatrosy.wsd.ontology;

import jade.content.Concept;

public class UserCard implements Concept {

    private String id;
    private Long x;
    private Long y;
    private String name;
    private int priority;
    private String description;

    public UserCard() {
        this.description = "";
    }

    public UserCard(String id, Long x, Long y, String name, int priority, String description) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.priority = priority;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
