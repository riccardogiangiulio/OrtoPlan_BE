package com.riccardo.giangiulio.auth.controller;

import com.riccardo.giangiulio.auth.util.JwtUtil;
import com.riccardo.giangiulio.dao.UserDAO;
import com.riccardo.giangiulio.models.User;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class AuthController {
    private final UserDAO userDAO = new UserDAO();

    public void registerRoutes(Javalin app) {
        app.post("/login", this::login);
    }

    public void login(Context ctx) {
        try {
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");

            if (email == null || password == null) {
                ctx.status(400).result("Email and password are required");
                return;
            }

            User user = userDAO.authenticateUser(email, password);
    
            if (user == null) {
                ctx.status(401).result("Email not registered");
                return;
            }
    
            String token = JwtUtil.generateToken(user.getEmail());
            ctx.json(new TokenResponse(token));
    
        } catch (Exception e) {
            ctx.status(400).result("Invalid request");
        }
    }

    private static class TokenResponse {
        public final String token;

        public TokenResponse(String token) {
            this.token = token;
        }
    }
}