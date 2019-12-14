package com.albatrosy.wsd.agents;

import jade.core.Agent;

public class UserAgent extends Agent {

    private Long x;
    private Long y;

    @Override
    protected void setup() {
        super.setup();
        initParameters();
    }

    private void initParameters() {
        Object[] args = getArguments();
        if (args.length != 2)
            throw new IllegalStateException("UserAgent must have two arguments");
        x = Long.parseLong(args[0].toString());
        y = Long.parseLong(args[1].toString());
    }
}
