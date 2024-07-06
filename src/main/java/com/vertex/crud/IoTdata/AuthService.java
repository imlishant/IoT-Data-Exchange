package com.vertex.crud.IoTdata;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;

public class AuthService {

    private final JWTAuth jwtAuth;

    public AuthService(Vertx vertx) {
        JsonObject config = new JsonObject()
            .put("keyStore", new JsonObject()
                .put("type", "jceks")
                .put("path", "keystore.jceks")
                .put("password", "secret"));

        this.jwtAuth = JWTAuth.create(vertx, new JWTAuthOptions()
            .addPubSecKey(new PubSecKeyOptions()
                .setAlgorithm("HS256")
                .setBuffer("keyboard cat")));
    }

    public JWTAuth getJwtAuth() {
        return jwtAuth;
    }

    public Future<String> authenticate(String username, String password) {
        if ("user".equals(username) && "password".equals(password)) {
            JsonObject token = new JsonObject()
                .put("username", username)
                .put("roles", "user");

            return Future.succeededFuture(jwtAuth.generateToken(token));
        } else {
            return Future.failedFuture("Authentication failed");
        }
    }
}
