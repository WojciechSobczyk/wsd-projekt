package com.albatrosy.wsd.adapters;

import org.jgrapht.graph.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import pl.sag.taxi.domain.common.Building;
import pl.sag.taxi.domain.common.Randomizer;
import pl.sag.taxi.ports.IGraphFactory;

import java.util.ArrayList;
import java.util.List;

@Component("graphFactory")
@Configuration
public class JGraphFactory  implements IGraphFactory {

    @Autowired
    private Randomizer randomizer;

    @Value("${xMapSize}")
    private int xMapSize;

    @Value("${yMapSize}")
    private int yMapSize;

    @Value("${minTime}")
    private int minTime;

    @Value(("${maxTime}"))
    private int maxTime;

    @Override
    @Bean
    public JGraph graph() {
        SimpleWeightedGraph<Building, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        List<Building> buildings = new ArrayList<>();
        for (int i = 0; i < xMapSize; i++) {
            for (int j = 0; j < yMapSize; j++) {
                Building building = new Building(i, j);
                graph.addVertex(building);
                buildings.add(building);
            }
        }
        randomizer.setMin(minTime);
        randomizer.setMax(maxTime);
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
