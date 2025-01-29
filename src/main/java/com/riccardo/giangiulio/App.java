package com.riccardo.giangiulio;

import com.riccardo.giangiulio.auth.controller.AuthController;
import com.riccardo.giangiulio.controller.ActivityController;
import com.riccardo.giangiulio.controller.ActivityTypeController;
import com.riccardo.giangiulio.controller.NotificationController;
import com.riccardo.giangiulio.controller.PlantController;
import com.riccardo.giangiulio.controller.PlantationController;
import com.riccardo.giangiulio.controller.UserController;
import com.riccardo.giangiulio.services.ActivityService;
import com.riccardo.giangiulio.services.ActivityTypeService;
import com.riccardo.giangiulio.services.NotificationService;
import com.riccardo.giangiulio.services.PlantService;
import com.riccardo.giangiulio.services.PlantationService;
import com.riccardo.giangiulio.services.UserService;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        }).start(7070);
        // rpova per Tag di JIRA
        UserService userService = new UserService();      
        PlantService plantService = new PlantService();
        PlantationService plantationService = new PlantationService();
        ActivityTypeService activityTypeService = new ActivityTypeService();
        ActivityService activityService = new ActivityService();
        NotificationService notificationService = new NotificationService();

        UserController userController = new UserController(userService);
        PlantController plantController = new PlantController(plantService);
        PlantationController plantationController = new PlantationController(plantationService);
        AuthController authController = new AuthController();
        ActivityTypeController activityTypeController = new ActivityTypeController(activityTypeService);
        ActivityController activityController = new ActivityController(activityService);
        NotificationController notificationController = new NotificationController(notificationService);

        userController.registerRoutes(app);
        plantController.registerRoutes(app);
        plantationController.registerRoutes(app);
        activityTypeController.registerRoutes(app);
        activityController.registerRoutes(app);
        notificationController.registerRoutes(app);
        authController.registerRoutes(app);
    }
}
