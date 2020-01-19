package com.albatrosy.wsd.agents;

import com.albatrosy.wsd.misc.IncidentMessageState;
import com.albatrosy.wsd.ontology.*;
import org.apache.commons.lang3.tuple.*;
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
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import lombok.extern.log4j.Log4j;

import java.util.*;

@Log4j
public class UserServiceDivisionAgent extends Agent {
    public static final String AGENT_TYPE = "user_service_division_agent";

    Ontology ontology = IncidentOntology.getInstance();
    private Map<AID, Pair<IncidentMessageState, List<UserIncidentMessage>>> incidents;


    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new Receiver());

        incidents = new HashMap<>();


        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
        getContentManager().registerOntology(ontology);

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(AGENT_TYPE);
        sd.setName(getName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    private void vettingCfp(ACLMessage message) {
        ACLMessage msg = new ACLMessage(ACLMessage.CFP);
        msg.setOntology(IncidentOntology.NAME);
        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
        UserDetails userDetails = new UserDetails(message.getSender().getLocalName());

        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(VerificationServiceDivisionAgent.AGENT_TYPE);
        dfAgentDescription.addServices(serviceDescription);

        try {
            DFAgentDescription[] addressees = DFService.search(this, dfAgentDescription);
            for (DFAgentDescription addressee : addressees) {
                msg.addReceiver(addressee.getName());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        try {
            getContentManager().fillContent(msg, new Action(this.getAID(), userDetails));
        } catch (Codec.CodecException | OntologyException e) {
            e.printStackTrace();
        }
        send(msg);
    }

    private void produceCard(UserVerdict userVerdict) {
        incidents.keySet()
                .stream()
                .filter(key -> key.getLocalName().equals(userVerdict.getUserName()))
                .findFirst()
                .ifPresent(id -> {
                    List<UserIncidentMessage> userIncidentMessageList = incidents.get(id).getRight();
                    UserIncidentMessage lastUserIncidentMessage = userIncidentMessageList.get(userIncidentMessageList.size() - 1);
                    if (userVerdict.getExist()) {
                        UserCard userCard = new UserCard(lastUserIncidentMessage.getId(), lastUserIncidentMessage.getX(), lastUserIncidentMessage.getY(), userVerdict.getUserName(), lastUserIncidentMessage.getIncidentPriority(), "");
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setOntology(IncidentOntology.NAME);
                        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                        msg.addReceiver(id);
                        try {
                            getContentManager().fillContent(msg, new Action(this.getAID(), userCard));
                        } catch (Codec.CodecException | OntologyException e) {
                            e.printStackTrace();
                        }
                        send(msg);
                    } else {
                        userIncidentMessageList.remove(userIncidentMessageList.size() - 1);
                    }
                });
    }

    private void vettingRequest(ACLMessage message) {
        ACLMessage response = message;
        response.clearAllReceiver();
        response.addReceiver(message.getSender());
        response.setSender(this.getAID());

        ContentElement element = null;
        try {
            element = getContentManager().extractContent(message);

        } catch (Codec.CodecException | OntologyException e) {
            e.printStackTrace();
        }
        Concept action = ((Action) element).getAction();
        String name = ((UserDetails) action).getName();

        AID aid = incidents.keySet()
                .stream()
                .filter(key -> key.getLocalName().equals(name))
                .findFirst().get();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(IncidentOntology.NAME);
        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
        msg.addReceiver(message.getSender());
        UserDetails userDetails = new UserDetails(message.getSender().getLocalName());
        try {
            getContentManager().fillContent(msg, new Action(this.getAID(), userDetails));
        } catch (Codec.CodecException | OntologyException e) {
            e.printStackTrace();
        }
//            if (true) { //TODO: filter incidents queue
        if (incidents.get(aid).getLeft().equals(IncidentMessageState.Initialized)) {
            response.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            incidents.put(aid, new MutablePair<>(IncidentMessageState.WaitingForVerification, incidents.get(aid).getRight()));
            log.info("Zaakcpetowano propozycje weryfikacji zglaszajacego");
        } else {
            response.setPerformative(ACLMessage.REJECT_PROPOSAL);
            log.info("Odrzucono propozycje weryfikacji zglaszajacego");
        }
        send(response);

//        }
    }

    private void sendUserIncidentCardToPriorityServiceDivisionAgent(UserCard userCard) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(IncidentOntology.NAME);
        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);

        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(PriorityServiceDivisionAgent.AGENT_TYPE);
        dfAgentDescription.addServices(serviceDescription);

        try {
            DFAgentDescription[] addressees = DFService.search(this, dfAgentDescription);
            for (DFAgentDescription addressee : addressees) {
                msg.addReceiver(addressee.getName());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        try {
            getContentManager().fillContent(msg, new Action(this.getAID(), userCard));
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
                    if (action instanceof UserIncidentMessage) {
                        addIncident(message, (UserIncidentMessage) action);
                        vettingCfp(message);
                        log.info("Otrzymano zgloszenie incydentu");
                    }
                    if (action instanceof UserDetails && message.getPerformative() == ACLMessage.PROPOSE) {
                        vettingRequest(message);
                        log.info("Otrzymano propozycje weryfikacji zglaszajacego");
                    }
                    if (action instanceof UserVerdict) {
                        UserVerdict userVerdict = (UserVerdict) action;
                        produceCard(userVerdict);
                        log.info("System produkuje karte incydentu");
                    }
                    if (action instanceof UserCard) {
                        log.info("Odebralem karte incydentu");
                        sendUserIncidentCardToPriorityServiceDivisionAgent(((UserCard) action));
                        log.info("Wyslalem karte incydentu do PriorityServiceDivisionAgent");
                    }
                } catch (OntologyException | Codec.CodecException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void addIncident(ACLMessage message, UserIncidentMessage action) {
        if (incidents.containsKey(message.getSender()))
            incidents.get(message.getSender()).getRight().add(action);
        else {
            List<UserIncidentMessage> newUserIncidentMessageList = new ArrayList<>();
            newUserIncidentMessageList.add(action);
            incidents.put(message.getSender(), new MutablePair<>(IncidentMessageState.Initialized, newUserIncidentMessageList));
        }

    }
}


