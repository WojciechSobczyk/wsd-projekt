package com.albatrosy.wsd.map;

import com.albatrosy.wsd.ports.IGraphPath;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jgrapht.GraphPath;

@AllArgsConstructor
@Getter
public class JGraphPath implements IGraphPath {
    private GraphPath graphPath;

    @Override
    public int getTime() {
        return (int) graphPath.getWeight();
    }
}
