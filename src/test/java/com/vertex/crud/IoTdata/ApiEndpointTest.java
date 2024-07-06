package com.vertex.crud.IoTdata;



import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ApiEndpointTest {

    private Vertx vertx;
    private WebClient webClient;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle(), context.asyncAssertSuccess());

        // Create a web client to test HTTP endpoints
        webClient = WebClient.create(vertx);
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testCreateDataEndpoint(TestContext context) {
        // Test POST /api/create endpoint
        JsonObject data = new JsonObject().put("id", 1).put("value", "Test Value");

        webClient.post(8800, "localhost", "/api/create")
                 .sendJson(data, context.asyncAssertSuccess(response -> {
                     context.assertEquals(201, response.statusCode());
                     context.assertEquals("Data created", response.bodyAsString().trim());
                 }));
    }

    @Test
    public void testGetDataByIdEndpoint(TestContext context) {
        // Test GET /api/read/:id endpoint
        webClient.get(8800, "localhost", "/api/read/1")
                 .send(context.asyncAssertSuccess(response -> {
                     context.assertEquals(200, response.statusCode());
                     JsonObject result = response.bodyAsJsonObject();
                     context.assertEquals(1, result.getInteger("id"));
                 }));
    }

    @Test
    public void testUpdateDataEndpoint(TestContext context) {
        // Test PUT /api/update/:id endpoint
        JsonObject data = new JsonObject().put("value", "Updated Value");

        webClient.put(8800, "localhost", "/api/update/1")
                 .sendJson(data, context.asyncAssertSuccess(response -> {
                     context.assertEquals(200, response.statusCode());
                     context.assertEquals("Data updated", response.bodyAsString().trim());
                 }));
    }

    @Test
    public void testDeleteDataEndpoint(TestContext context) {
        // Test DELETE /api/delete/:id endpoint
        webClient.delete(8800, "localhost", "/api/delete/1")
                 .send(context.asyncAssertSuccess(response -> {
                     context.assertEquals(200, response.statusCode());
                     context.assertEquals("Data deleted", response.bodyAsString().trim());
                 }));
    }
}
