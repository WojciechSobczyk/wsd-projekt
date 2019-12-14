package com.albatrosy.wsd.agents;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;

public class UserAgent extends Agent {

    private Long x;
    private Long y;

    @Override
    protected void setup() {
        super.setup();
        initParameters();
        addBehaviour(new Sender());
    }

    private void initParameters() {
        Object[] args = getArguments();
        if (args.length != 2)
            throw new IllegalStateException("UserAgent must have two arguments");
        x = Long.parseLong(args[0].toString());
        y = Long.parseLong(args[1].toString());
    }
    class Sender extends OneShotBehaviour {
        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("odbiorca", AID.ISLOCALNAME));
            msg.setLanguage("Polish");
            msg.setContent("Witaj");
            send(msg);
        }
    }
}
