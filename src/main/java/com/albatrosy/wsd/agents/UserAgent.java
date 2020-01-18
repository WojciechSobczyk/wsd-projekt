package com.albatrosy.wsd.agents;

import com.albatrosy.wsd.map.Building;
import com.albatrosy.wsd.map.CityMap;
import com.albatrosy.wsd.ontology.*;
import com.albatrosy.wsd.other.Descriptions;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class UserAgent extends Agent {

    public static final String AGENT_TYPE = "user_agent";

    private UserDetails userDetails;
    private Location location;
    private List<UserIncidentMessage> incidentMessages = new ArrayList<>();
    private AID userServiceDivisionAgentAid;

    @Setter
    private CityMap cityMap = CityMap.getInstance();

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
        log.info("Utworzono obywatela pozycja: " + location.toString());
    }

    private void initParameters() {
        userDetails = new UserDetails(this.getName());
        Building building = cityMap.getRandomBuilding();
        location = new Location((long) building.getX(), (long) building.getY());
    }

    private void reportIncident() {
        ACLMessage msg = initializeMessage();
        UserIncidentMessage incident = new UserIncidentMessage(location.getX(), location.getY(), IncidentPriority.HIGH); //TODO: smarter incident generation
        incidentMessages.add(incident);
        addUserServiceDivisionReceivers(msg);
        try {
            getContentManager().fillContent(msg, new Action(this.getAID(), incident));
        } catch (Codec.CodecException | OntologyException e) {
            e.printStackTrace();
        }
        send(msg);
    }

    private void sendUserIncidentCard (UserCard userCard) {
        userCard.setDescription(Descriptions.randomDescription());
        ACLMessage msg = initializeMessage();
        addUserServiceDivisionReceivers(msg);
        try {
            getContentManager().fillContent(msg, new Action(this.getAID(), userCard));
        } catch (Codec.CodecException | OntologyException e) {
            e.printStackTrace();
        }
        send(msg);
    }

    private ACLMessage initializeMessage() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setOntology(IncidentOntology.NAME);
        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
        return msg;
    }

    private void addUserServiceDivisionReceivers(ACLMessage msg) {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(UserServiceDivisionAgent.AGENT_TYPE);
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFAgentDescription[] addressees = DFService.search(this, dfAgentDescription); //TODO there is always one instance of UserServiceDivisionAgent, array is redundant
            for (DFAgentDescription addressee : addressees) {
                msg.addReceiver(addressee.getName());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
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
                if (message.getContent() != null) {
                    if (message.getContent().equals("incydent")) {
                        reportIncident();
                        log.info("Nastapil incydent!");
                        return;
                    }
                }
                try {
                    ContentElement element = myAgent.getContentManager().extractContent(message);
                    Concept action = ((Action) element).getAction();
                    if (action instanceof UserCard) {
                        log.info("Dostalem karte incydentu");
                        UserCard userCard = (UserCard) action;
                        sendUserIncidentCard(userCard);
                        log.info("Uzupelnilem opis incydentu: " + userCard.getDescription());
                    }
                } catch (Codec.CodecException | OntologyException e) {
                    e.getStackTrace();
                }
            }
        }
    }
}
