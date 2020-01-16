package com.albatrosy.wsd.adapters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jgrapht.GraphPath;
import pl.sag.taxi.ports.IGraphPath;

@AllArgsConstructor
@Getter
public class JGraphPath implements IGraphPath {
    private GraphPath graphPath;

    @Override
    public int getTime() {
        return (int) graphPath.getWeight();
    }
}
