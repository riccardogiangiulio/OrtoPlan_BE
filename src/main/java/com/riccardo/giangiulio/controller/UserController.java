package com.riccardo.giangiulio.controller;

import com.riccardo.giangiulio.models.User;
import com.riccardo.giangiulio.services.UserService;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void registerRoutes(Javalin app) {
        app.post("/users", this::registerUser);
        app.get("/users", this::getUserByEmail);
        app.get("/users/{userId}", this::getUserById);
        app.put("/users/{userId}", this::updateUser);
        app.put("/users/password/{userId}", this::updatePassword);
        app.delete("/users/{userId}", this::deleteUser);
    }

    private void registerUser(Context ctx) {
        try {
            User user = ctx.bodyAsClass(User.class);
            userService.registerUser(user);
            ctx.status(201).json(user);
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    private void getUserById(Context ctx) {
        try {
            long userId = Long.parseLong(ctx.pathParam("userId"));
            User user = userService.getUserById(userId);
            if (user != null) {
                ctx.json(user);
            } else {
                ctx.status(404).result("User not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID");
        }
    }

    private void getUserByEmail(Context ctx) {
        String email = ctx.queryParam("email");
        if (email == null) {
            ctx.status(400).result("Email parameter is required");
            return;
        }
        User user = userService.getUserByEmail(email);
        if (user != null) {
            ctx.json(user);
        } else {
            ctx.status(404).result("User not found");
        }
    }   

    private void updateUser(Context ctx) {
        try {
            long userId = Long.parseLong(ctx.pathParam("userId"));
            User updates = ctx.bodyAsClass(User.class);
            userService.updateUser(updates, userId);
            User updatedUser = userService.getUserById(userId);
            ctx.json(updatedUser);
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID");
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    private void updatePassword(Context ctx) {
        try {
            long userId = Long.parseLong(ctx.pathParam("userId"));
            String newPassword = ctx.body();
            userService.updatePassword(userId, newPassword);
            ctx.status(200).result("Password updated successfully");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID");
        }
    }

    private void deleteUser(Context ctx) {
        try {
            long userId = Long.parseLong(ctx.pathParam("userId"));
            userService.deleteUser(userId);
            ctx.status(204).result("User deleted successfully");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID");
        }
    }
}
