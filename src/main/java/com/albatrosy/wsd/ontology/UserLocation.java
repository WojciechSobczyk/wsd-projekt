package com.albatrosy.wsd.ontology;

import jade.content.Concept;

public class UserLocation implements Concept {
    private Long x;
    private Long y;

    public UserLocation() {
        this.x = 0L;
        this.y = 0L;
    }

    public UserLocation(Long x, Long y) {
        this.x = x;
        this.y = y;
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
}
