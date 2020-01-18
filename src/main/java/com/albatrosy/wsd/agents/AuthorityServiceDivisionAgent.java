package com.albatrosy.wsd.agents;

import com.albatrosy.wsd.map.Building;
import com.albatrosy.wsd.map.CityMap;
import com.albatrosy.wsd.ontology.*;
import com.albatrosy.wsd.ports.IGraphPath;
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
import lombok.extern.log4j.Log4j;

import java.util.Optional;

@Log4j
public class AuthorityServiceDivisionAgent extends Agent {
    public static final String AGENT_TYPE = "authority_service_division_agent";
    private AuthorityState authorityState;
    private Location location;
    private CityMap cityMap = CityMap.getInstance();
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

        log.info("Utworzono pracownika sluzb porzadkowych. Pozycja: " + location.toString());
    }

    private void initParameters() {
        Building building = cityMap.getRandomBuilding();
        location = new Location( (long) building.getX(), (long) building.getY());
    }

    private void sendApproval(ACLMessage message, UserCard userCard) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setOntology(IncidentOntology.NAME);
        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
        msg.addReceiver(message.getSender());
        Optional<Building> startOptional = cityMap.getBuilding(location.getX().intValue(), location.getY().intValue());
        Optional<Building> stopOptional = cityMap.getBuilding(userCard.getX().intValue(), userCard.getY().intValue());
        if (startOptional.isPresent() && stopOptional.isPresent()) {
            IGraphPath graphPath = cityMap.getShortestPath(startOptional.get(), stopOptional.get());
            double time = graphPath.getTime();
            AuthorityIncidentParameters authorityIncidentParameters = new AuthorityIncidentParameters(time);
            try {
                getContentManager().fillContent(msg, new Action(this.getAID(), authorityIncidentParameters));
            } catch (Codec.CodecException | OntologyException e) {
                e.printStackTrace();
            }
            //send(msg);
            log.info("Moj czas na dojazd to: " +  time + " minut");
        }
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
                    }
                } catch (OntologyException | Codec.CodecException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
