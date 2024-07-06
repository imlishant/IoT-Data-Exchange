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
public class AuthServiceTest {

    private Vertx vertx;
    private AuthService authService;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        authService = new AuthService(vertx);
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testSuccessfulAuthentication(TestContext context) {
        authService.authenticate("user", "password")
                   .onComplete(ar -> {
                       if (ar.succeeded()) {
                           context.assertNotNull(ar.result());
                           context.assertTrue(ar.result().length() > 0);
                       } else {
                           context.fail(ar.cause());
                       }
                   });
    }

    @Test
    public void testFailedAuthentication(TestContext context) {
        authService.authenticate("invalid_user", "invalid_password")
                   .onComplete(ar -> {
                       if (ar.failed()) {
                           context.assertEquals("Authentication failed", ar.cause().getMessage());
                       } else {
                           context.fail("Expected authentication failure");
                       }
                   });
    }
}
