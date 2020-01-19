package com.albatrosy.wsd.ontology;

import jade.content.Concept;

public class AuthorityIncidentParameters implements Concept {

    private String incidentId;
    private double time;
    private int authorityState;

    public AuthorityIncidentParameters() {
        incidentId = "";
    }

    public AuthorityIncidentParameters(String incidentId, double time, int authorityState) {
        this.incidentId = incidentId;
        this.time = time;
        this.authorityState = authorityState;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    public int getAuthorityState() {
        return authorityState;
    }

    public void setAuthorityState(int authorityState) {
        this.authorityState = authorityState;
    }
}
