package com.albatrosy.wsd.adapters;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JGraphXHelperVisualizerFactory {

    private JGraph jGraph;

    public JGraphHelperVisualizer jGraphXVisualizer () {
        JGraphHelperVisualizer jGraphHelperVisualizer = new JGraphHelperVisualizer();
        jGraphHelperVisualizer.setJGraph(jGraph);
        jGraphHelperVisualizer.init();
        return jGraphHelperVisualizer;
    }
}
