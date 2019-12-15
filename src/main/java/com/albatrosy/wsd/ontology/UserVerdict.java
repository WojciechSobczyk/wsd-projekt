package com.albatrosy.wsd.ontology;

import jade.content.Concept;

public class UserVerdict implements Concept {

    private String userName;
    private boolean exist;

    public UserVerdict() {
    }

    public UserVerdict(String userName, boolean exist) {
        this.userName = userName;
        this.exist = exist;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public boolean getExist() {
        return exist;
    }
}
