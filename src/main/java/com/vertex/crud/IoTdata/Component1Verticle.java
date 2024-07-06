package com.vertex.crud.IoTdata;

import io.vertx.core.AbstractVerticle;

public class Component1Verticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.eventBus().consumer("component1.message", message -> {
            System.out.println("Received message in Component1Verticle: " + message.body());
            // Process message or perform actions
        });
    }
}
