package com.vertex.crud.IoTdata;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

    private Map<Integer, IoTData> dataStore = new HashMap<>();

    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.post("/create").handler(this::createData);
        router.get("/read/:id").handler(this::getDataById);
        router.get("/readAll").handler(this::getAllData);
        router.put("/update/:id").handler(this::updateData);
        router.delete("/delete/:id").handler(this::deleteData);
        router.delete("/deleteAll").handler(this::deleteAllData);


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
            int id = json.getInteger("id");
            String value = json.getString("value");

            if (dataStore.containsKey(id)) {
                context.response().setStatusCode(400).end("ID already exists");
                return;
            }

            IoTData data = new IoTData(id, value);
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
                context.response().setStatusCode(200).end(new JsonObject().put("id", data.getId()).put("value", data.getData()).encode());
            } else {
                context.response().setStatusCode(404).end("Data not found");
            }
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }

    private void getAllData(RoutingContext context) {
        try {
            if (dataStore.isEmpty()) {
                context.response().setStatusCode(404).end("No data available");
            } else {
                JsonArray dataArray = dataStore.entrySet()
                        .stream()
                        .map(entry -> new JsonObject()
                            .put("id", entry.getKey())
                            .put("value", entry.getValue().getData()))
                        .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
                
                context.response().setStatusCode(200).end(dataArray.encode());
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
                data.setData(json.getString("value"));
                context.response().setStatusCode(204).end();
            } else {
                context.response().setStatusCode(404).end("Data not found");
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
                context.response().setStatusCode(404).end("Data not found");
            }
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }
    
    private void deleteAllData(RoutingContext context) {
        try {
            if (dataStore.isEmpty()) {
                context.response().setStatusCode(404).end("No data to delete");
            } else {
                dataStore.clear();
                context.response().setStatusCode(204).end();
            }
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }


    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new MainVerticle());
    }
}
