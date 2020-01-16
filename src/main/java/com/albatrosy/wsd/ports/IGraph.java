package com.albatrosy.wsd.ports;

import com.albatrosy.wsd.map.Building;

import java.util.Optional;

public interface IGraph {
    int getEdgeSize();
    int getVertexSize();
    IGraphPath getShortestPath(Building start, Building end);
    Building getRandomBuilding();
    Optional<Building> getBuilding(int x, int y);
}
