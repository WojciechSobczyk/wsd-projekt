package com.albatrosy.wsd.adapters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import pl.sag.taxi.domain.common.Building;
import pl.sag.taxi.domain.common.Randomizer;
import pl.sag.taxi.ports.IGraph;
import pl.sag.taxi.ports.IGraphPath;

import java.util.Set;

@AllArgsConstructor
public class JGraph implements IGraph {
    @Getter
    private SimpleWeightedGraph<Building, DefaultWeightedEdge> graph;
    private Randomizer randomizer;

    @Override
    public int getEdgeSize() {
        return graph.edgeSet().size();
    }

    @Override
    public int getVertexSize() {
        return graph.vertexSet().size();
    }

    @Override
    public IGraphPath getShortestPath(Building start, Building end) {
        DijkstraShortestPath<Building, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Building, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(start, end);
        return new JGraphPath(graphPath);
    }

    @Override
    public Building getRandomBuilding() {
        Set<Building> buildings = graph.vertexSet();
        randomizer.setMin(0);
        randomizer.setMax(buildings.size() - 1);
        int result = randomizer.random();
        int counter = 0;
        for (Building building : buildings) {
            if (counter == result) {
                return building;
            }
            counter++;
        }
        return null;
    }
}
