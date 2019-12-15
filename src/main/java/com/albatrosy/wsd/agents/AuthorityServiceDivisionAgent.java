package com.albatrosy.wsd.agents;

import com.albatrosy.wsd.ontology.AuthorityState;
import com.albatrosy.wsd.ontology.IncidentOntology;
import com.albatrosy.wsd.ontology.UserDetails;
import com.albatrosy.wsd.ontology.UserLocation;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;

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

    class Receiver extends CyclicBehaviour {

        @Override
        public void action() {

        }
    }
}
