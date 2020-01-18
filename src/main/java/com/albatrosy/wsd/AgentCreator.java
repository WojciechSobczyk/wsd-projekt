package com.albatrosy.wsd;

import com.albatrosy.wsd.agents.*;
import com.albatrosy.wsd.map.CityMap;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class AgentCreator extends Agent {

    private ContainerController containerController;

    private ClassLoader classLoader = AgentCreator.class.getClassLoader();

    public static final String separator = ";";
    private CityMap cityMap = CityMap.getInstance();


    @Override
    protected void setup() {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                containerController = getContainerController();
                createAgents(UserServiceDivisionAgent.class);
                createAgents(UserAgent.class);
                createAgents(VerificationServiceDivisionAgent.class);
                createAgents(AuthorityServiceDivisionAgent.class);
                createAgents(PriorityServiceDivisionAgent.class);
            }
        });
    }

    private void createAgents(Class className) {
        File agentsConfig = new File(classLoader.getResource(className.getSimpleName()).getFile());
        try (Scanner scanner = new Scanner(agentsConfig)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parameters = line.split(separator);
                AgentController agentController = containerController.createNewAgent
                        (parameters[0], className.getName(), Arrays.copyOfRange(parameters, 1, parameters.length));
                agentController.start();
            }
        } catch (FileNotFoundException | StaleProxyException e) {
            e.printStackTrace();
        }

    }
}
