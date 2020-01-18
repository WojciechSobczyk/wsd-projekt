package com.albatrosy.wsd.agents;

import com.albatrosy.wsd.map.CityMap;
import com.albatrosy.wsd.ontology.IncidentOntology;
import com.albatrosy.wsd.ontology.UserCard;
import com.albatrosy.wsd.ontology.UserDetails;
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
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
public class PriorityServiceDivisionAgent extends Agent {
    public static final String AGENT_TYPE = "priority_service_division_agent";

    @Setter
    private CityMap cityMap = CityMap.getInstance();

    Ontology ontology = IncidentOntology.getInstance();

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new Receiver());

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

    private void sendUserIncidentCardToAuthorities (UserCard userCard) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(IncidentOntology.NAME);
        msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);

        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(AuthorityServiceDivisionAgent.AGENT_TYPE);
        dfAgentDescription.addServices(serviceDescription);

        try{
            DFAgentDescription[] addressees = DFService.search(this, dfAgentDescription);
            for(DFAgentDescription addressee : addressees){
                msg.addReceiver(addressee.getName());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        try{
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
                    if (action instanceof UserCard) {
                        log.info("Otrzymalem karte incydentu");
                        UserCard userCard = (UserCard) action;
                        sendUserIncidentCardToAuthorities(userCard);
                        log.info("Przesylam karte incydentu do sluzb");
                    }
                } catch (OntologyException | Codec.CodecException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
