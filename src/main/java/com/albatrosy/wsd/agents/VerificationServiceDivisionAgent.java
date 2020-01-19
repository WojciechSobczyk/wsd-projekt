package com.albatrosy.wsd.agents;

import com.albatrosy.wsd.ontology.IncidentOntology;
import com.albatrosy.wsd.ontology.UserDetails;
import com.albatrosy.wsd.ontology.UserVerdict;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j
public class VerificationServiceDivisionAgent extends Agent {

    public static final String AGENT_TYPE = "verification_service_division_agent";
    private static final String REGISTERED_USERS_FILEPATH = "src/main/resources/RegisteredUsers";

    private List<String> registeredUsers;
    private Ontology ontology = IncidentOntology.getInstance();

    @Override
    protected void setup() {
        super.setup();
        registerAgent();
        initRegisteredUsers();
        addBehaviour(new Receiver());
    }

    private void registerAgent() {
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

    private void initRegisteredUsers() {
        try {
            registeredUsers = Files.lines(Paths.get(REGISTERED_USERS_FILEPATH)).collect(Collectors.toList());
        } catch (IOException e) {
            registeredUsers = new ArrayList<>();
            e.printStackTrace();
        }
    }

    private void vettingResponse(int aclMessageType, String userName, boolean exist, AID sender) {
        ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
        msg.setOntology(IncidentOntology.NAME);
        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
        UserVerdict userVerdict = new UserVerdict(userName, exist);

        msg.addReceiver(sender);
        try {
            getContentManager().fillContent(msg, new Action(this.getAID(), userVerdict));
        } catch (Codec.CodecException |
                OntologyException e) {
            e.printStackTrace();
        }
        send(msg);
    }

    private void vettingCfpResponse(ACLMessage message) {
        ACLMessage response = message;
        response.setPerformative(ACLMessage.PROPOSE);
        response.clearAllReceiver();
        response.addReceiver(message.getSender());
        response.setSender(this.getAID());
        send(response);
    }

    class Receiver extends CyclicBehaviour {

        @Override
        public void action() {
            ACLMessage message = receive();
            if (message != null) {
                try {
                    ContentElement element = myAgent.getContentManager().extractContent(message);
                    Concept action = ((Action) element).getAction();
                    if (action instanceof UserDetails && message.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                        UserDetails userDetails = (UserDetails) action;
                        String name = userDetails.getName();
                        Optional<String> searchedUser = registeredUsers.stream()
                                .filter(registeredUser -> registeredUser.equals(name))
                                .findFirst();
                        if (searchedUser.isPresent()) {
                            vettingResponse(ACLMessage.CONFIRM, name, true, message.getSender());
                            log.info("Zglaszajacy zweryfikowany pozytywnie");
                        } else {
                            vettingResponse(ACLMessage.DISCONFIRM, name, false, message.getSender());
                            log.info("Zglaszajacy zweryfikowany negatywnie");
                        }
                    }
                    if (action instanceof UserDetails && message.getPerformative() == ACLMessage.CFP) {
                        vettingCfpResponse(message);
                    }

                } catch (OntologyException | Codec.CodecException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

