package com.albatrosy.wsd.ontology;

import jade.content.Concept;

public class Location implements Concept {
    private Long x;
    private Long y;

    public Location() {
        this.x = 0L;
        this.y = 0L;
    }

    public Location(Long x, Long y) {
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

    @Override
    public String toString () {
        return "[" + x + "," + y + "]";
    }
}
