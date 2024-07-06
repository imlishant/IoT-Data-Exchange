package com.vertex.crud.IoTdata;

import io.vertx.core.AbstractVerticle;

public class Component2Verticle extends AbstractVerticle {

    @Override
    public void start() {
        // Send a message to Component1Verticle via the event bus periodically
        vertx.setPeriodic(5000, id -> {
            vertx.eventBus().send("component1.message", "Hello from Component2Verticle!");
        });
    }
}
