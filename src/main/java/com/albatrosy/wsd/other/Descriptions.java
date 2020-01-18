package com.albatrosy.wsd.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Descriptions {
    private static final String desc1 = "Picie alkoholu w miejcu publicznym";
    private static final String desc2 = "Bojka";
    private static final String desc3 = "Zaklocanie ciszy nocnej";
    private static final String desc4 = "Akty wandalizmu";
    private static final String desc5 = "Niszczenie mienia";

    public static String randomDescription () {
        Randomizer randomizer = new Randomizer();
        List<String> descriptionsList = new ArrayList<>(Arrays.asList(desc1, desc2, desc3, desc4, desc5));
        randomizer.setMin(0);
        randomizer.setMax(descriptionsList.size() - 1);
        return descriptionsList.get(randomizer.random());
    }
}
