package com.albatrosy.wsd.agents;
import com.albatrosy.wsd.ontology.*;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;

public class UserAgent extends Agent {

    public static final String AGENT_TYPE = "user_agent";

    private UserDetails userDetails;
    private UserLocation userLocation;
    private List<UserIncidentMessage> incidentMessages = new ArrayList<>();
    private AID userServiceDivisionAgentAid;

    private Long x;
    private Long y;

    Ontology ontology = IncidentOntology.getInstance();

    @Override
    protected void setup() {
        super.setup();
        initParameters();
        addBehaviour(new Sender());
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
        if (args.length != 2)
            throw new IllegalStateException("UserAgent must have two arguments");

        userDetails = new UserDetails(this.getName());
        userLocation = new UserLocation(Long.parseLong(args[0].toString()), Long.parseLong(args[1].toString()));

    }

    private void reportIncident(){
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setOntology(IncidentOntology.NAME);
        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
        UserIncidentMessage incident = new UserIncidentMessage(userLocation.getX(), userLocation.getY(), IncidentPriority.HIGH); //TODO: smarter incident generation
        incidentMessages.add(incident);

        AID receiver = new AID("UserServiceDivisionAgent", AID.ISLOCALNAME);

        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(UserServiceDivisionAgent.AGENT_TYPE);
        dfAgentDescription.addServices(serviceDescription);

        try{
            DFAgentDescription[] addressees = DFService.search(this, dfAgentDescription); //TODO there is always one instance of UserServiceDivisionAgent, array is redundant
            for(DFAgentDescription addressee : addressees){
                msg.addReceiver(addressee.getName());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        try{
            getContentManager().fillContent(msg, new Action(receiver, incident));
        } catch (Codec.CodecException | OntologyException e) {
            e.printStackTrace();
        }

        send(msg);
    }
    class Sender extends OneShotBehaviour {
        @Override
        public void action() {
//            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//            msg.addReceiver(new AID("odbiorca", AID.ISLOCALNAME));
//            msg.setLanguage("Polish");
//            msg.setContent("Witaj");
//            send(msg);
        }
    }


    class Receiver extends CyclicBehaviour {

        @Override
        public void action() {
            ACLMessage message = receive();
            if (message != null) {
                System.out.println("Trying to register incident");
                reportIncident();
            }
        }
    }
}
