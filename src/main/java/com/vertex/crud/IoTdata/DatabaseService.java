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
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("localhost")
                .setDatabase("testDB")
                .setUser("postgres")
                .setPassword("codingdnn8");
//                .setSsl(false);
//        		.setSslMode(PgSslMode.DISABLE);

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        this.client = PgPool.pool(vertx, connectOptions, poolOptions);
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
