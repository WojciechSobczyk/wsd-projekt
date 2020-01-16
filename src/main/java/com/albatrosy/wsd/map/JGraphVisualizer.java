package com.albatrosy.wsd.map;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JGraphVisualizer {

    private JGraphHelperVisualizer jGraphHelperVisualizer;

    public void visualize() {
        JFrame frame = new JFrame();
        frame.getContentPane().add(jGraphHelperVisualizer);
        frame.setTitle("JGraphT Adapter to JGraphX Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
