package com.albatrosy.wsd.ontology;

public enum AuthorityState {
    FREE(0),
    BUSY(1);

    private int value;

    AuthorityState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
