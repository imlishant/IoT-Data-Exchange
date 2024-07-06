package com.vertex.crud.IoTdata;


import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class Component1VerticleTest {

    private Vertx vertx;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new Component1Verticle(), context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testComponent1Verticle(TestContext context) {
        vertx.eventBus().request("component1.address", new JsonObject().put("key", "value"), reply -> {
            if (reply.succeeded()) {
                context.assertEquals("Expected Response", reply.result().body().toString());
            } else {
                context.fail("Failed to receive response");
            }
        });
    }
}
