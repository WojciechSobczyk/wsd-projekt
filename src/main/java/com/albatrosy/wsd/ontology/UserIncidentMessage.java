package com.albatrosy.wsd.ontology;

import jade.content.Concept;

public class UserIncidentMessage implements Concept {

    private Long x;
    private Long y;
    private int incidentPriority;

    public UserIncidentMessage() {
        this.x = 0L;
        this.y = 0L;
        this.incidentPriority = IncidentPriority.LOW.ordinal();
    }

    public UserIncidentMessage(Long x, Long y, IncidentPriority incidentPriority) {
        this.x = x;
        this.y = y;
        this.incidentPriority = incidentPriority.ordinal();
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

    public int getIncidentPriority() {
        return incidentPriority;
    }

    public void setIncidentPriority(int incidentPriority) {
        this.incidentPriority = incidentPriority;
    }
}
