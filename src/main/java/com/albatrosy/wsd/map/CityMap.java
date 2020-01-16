package com.albatrosy.wsd.map;

import com.albatrosy.wsd.ports.IGraph;
import com.albatrosy.wsd.ports.IGraphFactory;
import com.albatrosy.wsd.ports.IGraphPath;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.Optional;

@Log4j
@AllArgsConstructor
public class CityMap {
    private static CityMap INSTANCE;
    private IGraph graph;

    public static CityMap getInstance() {
        if (INSTANCE == null) {
            IGraphFactory graphFactory = new JGraphFactory();
            INSTANCE = new CityMap(graphFactory.graph());
        }
        return INSTANCE;
    }

    public IGraphPath getShortestPath(Building start, Building end) {
        return graph.getShortestPath(start, end);
    }
    public Building getRandomBuilding() {
        return graph.getRandomBuilding();
    }
    public Optional<Building> getBuilding(int x, int y) {
        return graph.getBuilding(x,y);
    }
}
