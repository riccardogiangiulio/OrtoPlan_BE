package com.riccardo.giangiulio;

import com.riccardo.giangiulio.auth.controller.AuthController;
import com.riccardo.giangiulio.controller.UserController;
import com.riccardo.giangiulio.services.UserService;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);

        UserService userService = new UserService();      
        UserController userController = new UserController(userService);
        AuthController authController = new AuthController();

        userController.registerRoutes(app);
        authController.registerRoutes(app);
    }
}
