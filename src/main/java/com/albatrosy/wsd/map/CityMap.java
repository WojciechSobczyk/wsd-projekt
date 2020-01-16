package com.albatrosy.wsd.map;

import com.albatrosy.wsd.ports.IGraph;
import com.albatrosy.wsd.ports.IGraphPath;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CityMap {
    private IGraph graph;

    public IGraphPath getShortestPath(Building start, Building end) {
        return graph.getShortestPath(start, end);
    }
    public Building getRandomBuilding() {
        return graph.getRandomBuilding();
    }
}
