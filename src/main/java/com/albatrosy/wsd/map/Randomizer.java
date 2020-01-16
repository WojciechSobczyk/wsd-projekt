package com.albatrosy.wsd.map;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Randomizer {
    private Random random = new Random();

    @Setter
    int min;

    @Setter
    int max;

    public int random() {
        return random.nextInt((max - min) + 1) + min;
    }
}
