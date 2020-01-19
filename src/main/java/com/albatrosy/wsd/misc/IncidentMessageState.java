package com.albatrosy.wsd.misc;

import com.albatrosy.wsd.ontology.UserIncidentMessage;
import jade.core.AID;

public enum IncidentMessageState {

        Initialized,
        WaitingForVerification,
        Verified,
        Rejected

}
