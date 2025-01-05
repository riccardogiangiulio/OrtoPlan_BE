package com.riccardo.giangiulio.auth.middleware;

import com.riccardo.giangiulio.auth.util.JwtUtil;

import io.javalin.http.Context;
import io.javalin.http.Handler;

// Classe che genera il Token, non dovreste modificarla in teoria a meno di non dover cambiare il campo "username"
public class JwtAuthMiddleware implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        String authHeader = ctx.header("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ctx.status(401).result("Missing or invalid token");
            return;
        }

        String token = authHeader.replace("Bearer ", "");
        try {
            String email = JwtUtil.validateToken(token);
            ctx.attribute("email", email);
        } catch (Exception e) {
            ctx.status(401).result("Invalid or expired token");
        }
    }
}
