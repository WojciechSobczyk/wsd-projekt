package com.albatrosy.wsd.ports;

import com.albatrosy.wsd.map.Building;

public interface IGraph {
    int getEdgeSize();
    int getVertexSize();
    IGraphPath getShortestPath(Building start, Building end);
    Building getRandomBuilding();
}
