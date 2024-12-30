package com.riccardo.giangiulio.controller;

import com.riccardo.giangiulio.models.User;
import com.riccardo.giangiulio.services.UserService;

import io.javalin.Javalin;

public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void registerRoutes(Javalin app) {
        app.post("/user", ctx -> {
            userService.registerUser(ctx.bodyAsClass(User.class));
        });

        app.get("/user/{userId}", ctx -> {
            try {
                User user = userService.getUserById(Long.parseLong(ctx.pathParam("userId")));
                if (user != null) {
                    ctx.json(user);  // Restituisce l'utente come JSON
                } else {
                    ctx.status(404).result("User not found");
                }
            } catch (NumberFormatException e) {
                ctx.status(400).result("Invalid user ID");
            }
        });
        
    } 

 
}
