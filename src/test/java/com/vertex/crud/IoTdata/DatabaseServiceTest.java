package com.vertex.crud.IoTdata;



import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class DatabaseServiceTest {

    private Vertx vertx;
    private DatabaseService databaseService;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        databaseService = new DatabaseService(vertx);
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testCreateData(TestContext context) {
        Async async = context.async();
        databaseService.createData(1, "Test Value")
                        .onComplete(ar -> {
                            if (ar.succeeded()) {
                                async.complete();
                            } else {
                                context.fail(ar.cause());
                            }
                        });
        async.awaitSuccess();
    }

    @Test
    public void testGetDataById(TestContext context) {
        Async async = context.async();
        databaseService.getDataById(1)
                        .onComplete(ar -> {
                            if (ar.succeeded()) {
                                JsonObject result = ar.result();
                                context.assertEquals(1, result.getInteger("id"));
                                async.complete();
                            } else {
                                context.fail(ar.cause());
                            }
                        });
        async.awaitSuccess();
    }

    @Test
    public void testGetAllData(TestContext context) {
        Async async = context.async();
        databaseService.getAllData()
                        .onComplete(ar -> {
                            if (ar.succeeded()) {
                                JsonArray resultArray = ar.result();
                                context.assertFalse(resultArray.isEmpty());
                                async.complete();
                            } else {
                                context.fail(ar.cause());
                            }
                        });
        async.awaitSuccess();
    }

    @Test
    public void testUpdateData(TestContext context) {
        Async async = context.async();
        databaseService.updateData(1, "Updated Value")
                        .onComplete(ar -> {
                            if (ar.succeeded()) {
                                async.complete();
                            } else {
                                context.fail(ar.cause());
                            }
                        });
        async.awaitSuccess();
    }

    @Test
    public void testDeleteData(TestContext context) {
        Async async = context.async();
        databaseService.deleteData(1)
                        .onComplete(ar -> {
                            if (ar.succeeded()) {
                                async.complete();
                            } else {
                                context.fail(ar.cause());
                            }
                        });
        async.awaitSuccess();
    }

    @Test
    public void testDeleteAllData(TestContext context) {
        Async async = context.async();
        databaseService.deleteAllData()
                        .onComplete(ar -> {
                            if (ar.succeeded()) {
                                async.complete();
                            } else {
                                context.fail(ar.cause());
                            }
                        });
        async.awaitSuccess();
    }
}
