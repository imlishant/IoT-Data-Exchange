package com.vertex.crud.IoTdata;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainVerticle extends AbstractVerticle {

    private Map<Integer, IoTData> dataStore = new HashMap<>();
    private AtomicInteger counter = new AtomicInteger();

    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.post("/api/iotdata").handler(this::createData);
        router.get("/api/iotdata/:id").handler(this::getDataById);
        router.put("/api/iotdata/:id").handler(this::updateData);
        router.delete("/api/iotdata/:id").handler(this::deleteData);

        vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }

    private void createData(RoutingContext context) {
        try {
            JsonObject json = context.getBodyAsJson();
            int id = counter.incrementAndGet();
            IoTData data = new IoTData(id, json.getString("data"));
            dataStore.put(id, data);
            context.response().setStatusCode(201).end(new JsonObject().put("id", id).encode());
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }

    private void getDataById(RoutingContext context) {
        try {
            int id = Integer.parseInt(context.pathParam("id"));
            IoTData data = dataStore.get(id);
            if (data != null) {
                context.response().setStatusCode(200).end(new JsonObject().put("id", data.getId()).put("data", data.getData()).encode());
            } else {
                context.response().setStatusCode(404).end();
            }
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }

    private void updateData(RoutingContext context) {
        try {
            int id = Integer.parseInt(context.pathParam("id"));
            IoTData data = dataStore.get(id);
            if (data != null) {
                JsonObject json = context.getBodyAsJson();
                data.setData(json.getString("data"));
                context.response().setStatusCode(204).end();
            } else {
                context.response().setStatusCode(404).end();
            }
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }

    private void deleteData(RoutingContext context) {
        try {
            int id = Integer.parseInt(context.pathParam("id"));
            IoTData data = dataStore.remove(id);
            if (data != null) {
                context.response().setStatusCode(204).end();
            } else {
                context.response().setStatusCode(404).end();
            }
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new MainVerticle());
    }
}
