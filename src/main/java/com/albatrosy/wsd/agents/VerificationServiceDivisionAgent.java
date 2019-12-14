package com.albatrosy.wsd.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class VerificationServiceDivisionAgent extends Agent {
    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new Receiver());
    }

    class Receiver extends CyclicBehaviour {

        @Override
        public void action() {
            ACLMessage message = receive();
            if (message != null) {
                System.out.println("dostalem wiadomosc: veryfikacja");
            }
        }
    }
}
