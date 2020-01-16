package com.albatrosy.wsd.adapters;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import lombok.Setter;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import pl.sag.taxi.domain.common.Building;

import javax.swing.*;
import java.awt.*;

public class JGraphHelperVisualizer extends JApplet {

    private static final long serialVersionUID = 2202072534703043194L;
    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);
    private JGraphXAdapter<Building, DefaultWeightedEdge> jgxAdapter;
    @Setter
    private JGraph jGraph;

    @Override
    public void init() {
        ListenableGraph<Building, DefaultWeightedEdge> g =
                new DefaultListenableGraph<>(jGraph.getGraph());
        jgxAdapter = new JGraphXAdapter<>(g);
        setPreferredSize(DEFAULT_SIZE);
        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        getContentPane().add(component);
        resize(DEFAULT_SIZE);
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
        int radius = 100;
        layout.setX0((DEFAULT_SIZE.width / 2.0) - radius);
        layout.setY0((DEFAULT_SIZE.height / 2.0) - radius);
        layout.setRadius(radius);
        layout.setMoveCircle(true);
        layout.execute(jgxAdapter.getDefaultParent());
    }
}