package com.albatrosy.wsd.ontology;

import jade.content.Concept;

public class AuthorityIncidentParameters implements Concept {

    private double distance;

    public AuthorityIncidentParameters(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
