package com.albatrosy.wsd.map;

import com.albatrosy.wsd.ports.IGraphFactory;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JGraphFactory  implements IGraphFactory {

    private Randomizer randomizer;
    private String xMapSize;
    private String yMapSize;
    private String minTime;
    private String maxTime;

    public JGraphFactory() {
        randomizer = new Randomizer();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/application.properties"));
            xMapSize = properties.getProperty("xMapSize");
            yMapSize = properties.getProperty("yMapSize");
            minTime = properties.getProperty("minTime");
            maxTime = properties.getProperty("maxTime");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JGraph graph() {
        SimpleWeightedGraph<Building, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        List<Building> buildings = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(xMapSize); i++) {
            for (int j = 0; j < Integer.parseInt(yMapSize); j++) {
                Building building = new Building(i, j);
                graph.addVertex(building);
                buildings.add(building);
            }
        }
        randomizer.setMin(Integer.parseInt(minTime));
        randomizer.setMax(Integer.parseInt(maxTime));
        buildings.forEach(building -> {
            buildings.stream()
                    .filter(targetBuilding -> (targetBuilding.getX() - 1 == building.getX() && targetBuilding.getY() == building.getY()) ||
                            ((targetBuilding.getY() - 1 == building.getY()) && (targetBuilding.getX() == building.getX())))
                    .forEach(findedTargetBuilding -> {
                        DefaultWeightedEdge edge = graph.addEdge(building, findedTargetBuilding);
                        double time = randomizer.random();
                        graph.setEdgeWeight(edge, time);
                    });
        });
        return new JGraph(graph, randomizer);
    }
}
