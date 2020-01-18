package com.albatrosy.wsd.map;

import com.albatrosy.wsd.other.Randomizer;
import com.albatrosy.wsd.ports.IGraph;
import com.albatrosy.wsd.ports.IGraphPath;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
class JGraph implements IGraph {
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

    @Override
    public Optional<Building> getBuilding(int x, int y) {
        return graph.vertexSet()
                .stream()
                .filter((building) -> (building.getX() == x && building.getY() == y))
                .findFirst();
    }

    public void print () {
/*        double numberOfRowsAndColumns = Math.sqrt(getVertexSize());
        for (int i = 0; i < numberOfRowsAndColumns; i++) {
            if (getBuilding(0,i).isPresent() && getBuilding(0, i+1).isPresent()) {
                int time = getShortestPath(getBuilding(0, i).get(), getBuilding(0, i + 1).get()).getTime();
                System.out.print("[" + 0 + "," + i + "]--" + time + "--");
            }
            else {
                System.out.println("[" + 0 + "," + i + "]");
                System.out.print("  |    |");
            }
        }*/
    }
}
