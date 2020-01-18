package com.albatrosy.wsd.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PrimitiveSchema;

public class IncidentOntology extends Ontology {

    public static final String NAME = "incident-ontology";

    //UserCard
    public static final String USER_CARD = "UserCard";
    public static final String USER_CARD_X = "x";
    public static final String USER_CARD_Y = "y";
    public static final String USER_CARD_NAME = "name";
    public static final String USER_CARD_PRIORITY = "priority";
    public static final String USER_CARD_DESCRIPTION = "description";

    //UserDetails
    public static final String USER_DETAILS = "UserDetails";
    public static final String USER_DETAILS_NAME = "name";

    //UserIncidentMessage
    public static final String USER_INCIDENT_MESSAGE = "UserIncidentMessage";
    public static final String USER_INCIDENT_MESSAGE_ID = "id";
    public static final String USER_INCIDENT_MESSAGE_X = "x";
    public static final String USER_INCIDENT_MESSAGE_Y = "y";
    public static final String USER_INCIDENT_MESSAGE_PRIORITY = "incidentPriority";

    //UserLocation
    public static final String USER_LOCATION = "UserLocation";
    public static final String USER_LOCATION_X = "x";
    public static final String USER_LOCATION_Y = "y";

    //UserVerdict
    public static final String USER_VERDICT = "UserVerdict";
    public static final String USER_VERDICT_EXIST = "exist";
    public static final String USER_VERDICT_USER_NAME = "userName";

    //AuthorityIncidentParameters
    public static final String AUTHORITY_INCIDENT_PARAMETERS = "AuthorityIncidentParameters";
    public static final String AUTHORITY_INCIDENT_PARAMETERS_DISTANCE = "distance";


    public static Ontology instance = new IncidentOntology();

    public IncidentOntology() {
        super(NAME, BasicOntology.getInstance());

        try {
            add(new ConceptSchema(USER_CARD), UserCard.class);
            ConceptSchema conceptSchemaUserCard = (ConceptSchema) getSchema(USER_CARD);
            conceptSchemaUserCard.add(USER_CARD_X, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            conceptSchemaUserCard.add(USER_CARD_Y, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            conceptSchemaUserCard.add(USER_CARD_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            conceptSchemaUserCard.add(USER_CARD_PRIORITY, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            conceptSchemaUserCard.add(USER_CARD_DESCRIPTION, (PrimitiveSchema) getSchema(BasicOntology.STRING));

            add(new ConceptSchema(USER_DETAILS), UserDetails.class);
            ConceptSchema conceptSchemaUserDetails = (ConceptSchema) getSchema(USER_DETAILS);
            conceptSchemaUserDetails.add(USER_DETAILS_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));

            add(new ConceptSchema(USER_INCIDENT_MESSAGE), UserIncidentMessage.class);
            ConceptSchema conceptSchemaUserIncidentMessage = (ConceptSchema) getSchema(USER_INCIDENT_MESSAGE);
            conceptSchemaUserIncidentMessage.add(USER_INCIDENT_MESSAGE_ID, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            conceptSchemaUserIncidentMessage.add(USER_INCIDENT_MESSAGE_X, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            conceptSchemaUserIncidentMessage.add(USER_INCIDENT_MESSAGE_Y, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            conceptSchemaUserIncidentMessage.add(USER_INCIDENT_MESSAGE_PRIORITY, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

            add(new ConceptSchema(USER_LOCATION), Location.class);
            ConceptSchema conceptSchemaUserLocation = (ConceptSchema) getSchema(USER_LOCATION);
            conceptSchemaUserLocation.add(USER_LOCATION_X, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            conceptSchemaUserLocation.add(USER_LOCATION_Y, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

            add(new ConceptSchema(USER_VERDICT), UserVerdict.class);
            ConceptSchema conceptSchemaUserVerdict = (ConceptSchema) getSchema(USER_VERDICT);
            conceptSchemaUserVerdict.add(USER_VERDICT_EXIST, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN));
            conceptSchemaUserVerdict.add(USER_VERDICT_USER_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));

            add(new ConceptSchema(AUTHORITY_INCIDENT_PARAMETERS), AuthorityIncidentParameters.class);
            ConceptSchema conceptSchemaAuthorityIncidentParameters = (ConceptSchema) getSchema(AUTHORITY_INCIDENT_PARAMETERS);
            conceptSchemaAuthorityIncidentParameters.add(AUTHORITY_INCIDENT_PARAMETERS_DISTANCE, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));


        } catch (OntologyException ontologyException) {
            ontologyException.printStackTrace();
        }

    }

    public static Ontology getInstance() {
        return instance;
    }
}
