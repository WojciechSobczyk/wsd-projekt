package com.albatrosy.wsd.ports;

import pl.sag.taxi.domain.common.Building;

public interface IGraph {
    int getEdgeSize();
    int getVertexSize();
    IGraphPath getShortestPath(Building start, Building end);
    Building getRandomBuilding();
}
