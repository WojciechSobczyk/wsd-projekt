package com.albatrosy.wsd.agents;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;

public class UserAgent extends Agent {

    private Long x;
    private Long y;
    private int priority = 1;

    @Override
    protected void setup() {
        super.setup();
        initParameters();
        System.out.println ("Tu agent " + getAID().getName()+ "!");
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
            //ACLMessage msg = new ACLMessage(ACLMessage .REQUEST);
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            AID dest  =new AID("odbiorca", AID.ISLOCALNAME);//AID name umeszczamy nazwe agenta
            String name = getAID().getName();
            msg.addReceiver(dest);
            msg.setLanguage("Polish");
            msg.setContent(priority + " " + name);
            //msg.setOntology("home−dictionary "); ontologia nie jest konieczna jesli system jest zamkniety
            //msg. setReplyWith ("door_001" );
            send(msg);
        }
    }
}
