package com.vertex.crud.IoTdata;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;
//import io.vertx.pgclient.PgSslMode;

public class DatabaseService {

    private final PgPool client;

    public DatabaseService(Vertx vertx) {
    	/*
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(Integer.parseInt(System.getenv("DB_PORT")))
                .setHost(System.getenv("DB_HOST"))
                .setDatabase(System.getenv("DB_NAME"))
                .setUser(System.getenv("DB_USER"))
                .setPassword(System.getenv("DB_PASSWORD"));
//                .setSsl(false);
//        		.setSslMode(PgSslMode.DISABLE);

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        this.client = PgPool.pool(vertx, connectOptions, poolOptions);
        */
    	
        String host = System.getenv("DB_HOST");
        String portStr = System.getenv("DB_PORT");
        String database = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        

        // Ensure the environment variables are not null
        if (host == null) throw new IllegalArgumentException("DB_HOST environment variable is not set");
        if (portStr == null) throw new IllegalArgumentException("DB_PORT environment variable is not set");
        if (database == null) throw new IllegalArgumentException("DB_NAME environment variable is not set");
        if (user == null) throw new IllegalArgumentException("DB_USER environment variable is not set");
        if (password == null) throw new IllegalArgumentException("DB_PASSWORD environment variable is not set");


        // Set default port if environment variable is not set
        int port = 5432;
        if (portStr != null && !portStr.isEmpty()) {
            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                // Log the error and continue with the default port
                System.err.println("Invalid DB_PORT value, using default port 5432");
            }
        }

        PgConnectOptions connectOptions = new PgConnectOptions()
            .setHost(host)
            .setPort(port)
            .setDatabase(database)
            .setUser(user)
            .setPassword(password);

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        client = PgPool.pool(vertx, connectOptions, poolOptions);
    
    	
    }

    public Future<Void> createData(int id, String value) {
        Promise<Void> promise = Promise.promise();
        client.preparedQuery("INSERT INTO iot_data (id, value) VALUES ($1, $2)")
                .execute(Tuple.of(id, value), ar -> {
                    if (ar.succeeded()) {
                        promise.complete();
                    } else {
                        promise.fail(ar.cause());
                    }
                });
        return promise.future();
    }

    public Future<JsonObject> getDataById(int id) {
        Promise<JsonObject> promise = Promise.promise();
        client.preparedQuery("SELECT id, value FROM iot_data WHERE id = $1")
                .execute(Tuple.of(id), ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> result = ar.result();
                        if (result.size() == 0) {
                            promise.fail("Data not found");
                        } else {
                            Row row = result.iterator().next();
                            JsonObject jsonObject = new JsonObject()
                                    .put("id", row.getInteger("id"))
                                    .put("value", row.getString("value"));
                            promise.complete(jsonObject);
                        }
                    } else {
                        promise.fail(ar.cause());
                    }
                });
        return promise.future();
    }

    public Future<JsonArray> getAllData() {
        Promise<JsonArray> promise = Promise.promise();
        client.query("SELECT id, value FROM iot_data")
                .execute(ar -> {
                    if (ar.succeeded()) {
                        JsonArray dataArray = new JsonArray();
                        ar.result().forEach(row -> {
                            JsonObject jsonObject = new JsonObject()
                                    .put("id", row.getInteger("id"))
                                    .put("value", row.getString("value"));
                            dataArray.add(jsonObject);
                        });
                        promise.complete(dataArray);
                    } else {
                        promise.fail(ar.cause());
                    }
                });
        return promise.future();
    }

    public Future<Void> updateData(int id, String value) {
        Promise<Void> promise = Promise.promise();
        client.preparedQuery("UPDATE iot_data SET value = $1 WHERE id = $2")
                .execute(Tuple.of(value, id), ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().rowCount() == 0) {
                            promise.fail("Data not found");
                        } else {
                            promise.complete();
                        }
                    } else {
                        promise.fail(ar.cause());
                    }
                });
        return promise.future();
    }

    public Future<Void> deleteData(int id) {
        Promise<Void> promise = Promise.promise();
        client.preparedQuery("DELETE FROM iot_data WHERE id = $1")
                .execute(Tuple.of(id), ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().rowCount() == 0) {
                            promise.fail("Data not found");
                        } else {
                            promise.complete();
                        }
                    } else {
                        promise.fail(ar.cause());
                    }
                });
        return promise.future();
    }

    public Future<Void> deleteAllData() {
        Promise<Void> promise = Promise.promise();
        client.query("DELETE FROM iot_data")
                .execute(ar -> {
                    if (ar.succeeded()) {
                        promise.complete();
                    } else {
                        promise.fail(ar.cause());
                    }
                });
        return promise.future();
    }
}