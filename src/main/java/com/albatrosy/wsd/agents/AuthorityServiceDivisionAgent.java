package com.albatrosy.wsd.agents;

import com.albatrosy.wsd.ontology.*;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public class AuthorityServiceDivisionAgent extends Agent {

    public static final String AGENT_TYPE = "authority_service_division_agent";

    private AuthorityState authorityState;
    private Long x;
    private Long y;

    Ontology ontology = IncidentOntology.getInstance();

    @Override
    protected void setup() {
        super.setup();
        initParameters();
        addBehaviour(new Receiver());

        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
        getContentManager().registerOntology(ontology);

        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(AGENT_TYPE);
        serviceDescription.setName(getName());
        dfAgentDescription.addServices(serviceDescription);

        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    private void initParameters() {
        Object[] args = getArguments();
        if (args.length != 3)
            throw new IllegalStateException("UserAgent must have two arguments");

        if (args[0].equals("BUSY"))
            authorityState = AuthorityState.BUSY;
        else
            authorityState = AuthorityState.FREE;

        x = Long.parseLong(args[1].toString());
        y = Long.parseLong(args[2].toString());
    }

    private void sendApproval(ACLMessage message, UserCard userCard) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setOntology(IncidentOntology.NAME);
        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
        msg.addReceiver(message.getSender());
        double distance = Math.sqrt((userCard.getX() - x)^2 + (userCard.getY() - y)^2);
        AuthorityIncidentParameters authorityIncidentParameters = new AuthorityIncidentParameters(distance);
        try{
            getContentManager().fillContent(msg, new Action(this.getAID(), authorityIncidentParameters));
        } catch (Codec.CodecException | OntologyException e) {
            e.printStackTrace();
        }
        send(msg);
    }

    class Receiver extends CyclicBehaviour {

        @Override
        public void action() {
            ACLMessage message = receive();
            if (message != null) {
                try {
                    ContentElement element = myAgent.getContentManager().extractContent(message);
                    Concept action = ((Action) element).getAction();
                    if (action instanceof UserCard) {
                        UserCard userCard = (UserCard) action;
                        sendApproval(message, userCard);
                        System.out.println("Incident received");
                    }
                } catch (OntologyException | Codec.CodecException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
