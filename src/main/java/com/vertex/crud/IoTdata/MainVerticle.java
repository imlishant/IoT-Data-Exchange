package com.vertex.crud.IoTdata;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;

public class MainVerticle extends AbstractVerticle {

    private DatabaseService databaseService;
    private AuthService authService;

    @Override
    public void start(Promise<Void> startPromise) {
    	

        databaseService = new DatabaseService(vertx);
        authService = new AuthService(vertx);
        JWTAuth jwtAuth = authService.getJwtAuth();

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        // Public routes
        router.post("/login").handler(this::login);

        // Private routes
        router.route("/api/*").handler(JWTAuthHandler.create(jwtAuth));
        
        router.post("/api/create").handler(this::createData);
        router.get("/api/read/:id").handler(this::getDataById);
        router.get("/api/readAll").handler(this::getAllData);
        router.put("/api/update/:id").handler(this::updateData);
        router.delete("/api/delete/:id").handler(this::deleteData);
        router.delete("/api/deleteAll").handler(this::deleteAllData);
        
        // Deploy Component1Verticle
        vertx.deployVerticle(new Component1Verticle(), ar -> {
            if (ar.succeeded()) {
                System.out.println("Component1Verticle deployed successfully");
            } else {
                ar.cause().printStackTrace();
            }
        });

        // Deploy Component2Verticle
        vertx.deployVerticle(new Component2Verticle(), ar -> {
            if (ar.succeeded()) {
                System.out.println("Component2Verticle deployed successfully");
            } else {
                ar.cause().printStackTrace();
            }
        });

        vertx.createHttpServer().requestHandler(router).listen(8800, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8800");
            } else {
                System.out.println("HTTP server NOT started.");
                startPromise.fail(http.cause());
                System.out.println("HTTP server NOT started.");
            }
        });
    }
    
    
    

    private void login(RoutingContext context) {
        JsonObject credentials = context.getBodyAsJson();
        String username = credentials.getString("username");
        String password = credentials.getString("password");

        authService.authenticate(username, password).onComplete(ar -> {
            if (ar.succeeded()) {
                context.response().setStatusCode(200).end(new JsonObject().put("token", ar.result()).encode());
            } else {
                context.response().setStatusCode(401).end(ar.cause().getMessage());
            }
        });
    }

    private void createData(RoutingContext context) {
        try {
            JsonObject json = context.getBodyAsJson();
            int id = json.getInteger("id");
            String value = json.getString("value");

            databaseService.createData(id, value).onComplete(ar -> {
                if (ar.succeeded()) {
                    context.response().setStatusCode(201).end("Data created");
                } else {
                    context.response().setStatusCode(500).end(ar.cause().getMessage());
                }
            });
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }

    private void getDataById(RoutingContext context) {
        try {
            int id = Integer.parseInt(context.pathParam("id"));
            databaseService.getDataById(id).onComplete(ar -> {
                if (ar.succeeded()) {
                    context.response().setStatusCode(200).end(ar.result().encode());
                } else {
                    context.response().setStatusCode(404).end(ar.cause().getMessage());
                }
            });
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }

    private void getAllData(RoutingContext context) {

//    	System.out.println("issue");
//    	/*
        try {
            databaseService.getAllData().onComplete(ar -> {
                if (ar.succeeded()) {
                	System.out.println("issue");
                    context.response().setStatusCode(200).end(ar.result().encode());
                } else {
                    context.response().setStatusCode(500).end(ar.cause().getMessage());
                }
            });
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
//        */
    	
    }

    private void updateData(RoutingContext context) {
        try {
            int id = Integer.parseInt(context.pathParam("id"));
            JsonObject json = context.getBodyAsJson();
            String value = json.getString("value");

            databaseService.updateData(id, value).onComplete(ar -> {
                if (ar.succeeded()) {
                    context.response().setStatusCode(200).end("Data updated");
                } else {
                    context.response().setStatusCode(404).end(ar.cause().getMessage());
                }
            });
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }

    private void deleteData(RoutingContext context) {
        try {
            int id = Integer.parseInt(context.pathParam("id"));

            databaseService.deleteData(id).onComplete(ar -> {
                if (ar.succeeded()) {
                    context.response().setStatusCode(200).end("Data deleted");
                } else {
                    context.response().setStatusCode(404).end(ar.cause().getMessage());
                }
            });
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }

    private void deleteAllData(RoutingContext context) {
        try {
            databaseService.deleteAllData().onComplete(ar -> {
                if (ar.succeeded()) {
                    context.response().setStatusCode(200).end("All data deleted");
                } else {
                    context.response().setStatusCode(500).end(ar.cause().getMessage());
                }
            });
        } catch (Exception e) {
            context.response().setStatusCode(500).end(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new MainVerticle());
    }
}
