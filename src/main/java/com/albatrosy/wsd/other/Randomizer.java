package com.albatrosy.wsd.other;

import lombok.Setter;

import java.util.Random;

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
