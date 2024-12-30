package com.riccardo.giangiulio.auth.controller;

import org.mindrot.jbcrypt.BCrypt;

import com.riccardo.giangiulio.auth.util.JwtUtil;
import com.riccardo.giangiulio.dao.UserDAO;
import com.riccardo.giangiulio.models.User;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class AuthController {
    private final UserDAO userDAO = new UserDAO();

    // Rotta per login, cioè per ottenere il JWT token
    public void registerRoutes(Javalin app) {
        app.post("/login", this::login);
    }

    public void login(Context ctx) {
        try {
            // Ottieni email e password dai parametri del form
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");

            User user = userDAO.getUserByEmail(email);
    
            // Se l'utente non esiste, restituisci errore
            if (user == null) {
                ctx.status(401).result("Email non registrata");
                return;
            }
            if (!BCrypt.checkpw(password, user.getPassword())) {
                ctx.status(401).result("Email o password non corrette");
                return;
            }
    
            // Se le credenziali sono corrette, genera un token JWT
            String token = JwtUtil.generateToken(user.getEmail());
    
            // Restituisci il token al client
            ctx.json(new TokenResponse(token));
    
        } catch (Exception e) {
            // Gestione degli errori
            ctx.status(400).result("Richiesta non valida");
        }
    }


    private static class TokenResponse {
        public final String token;

        public TokenResponse(String token) {
            this.token = token;
        }
    }
}