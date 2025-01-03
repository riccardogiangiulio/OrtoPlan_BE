package com.riccardo.giangiulio.controller;

import com.riccardo.giangiulio.models.ActivityType;
import com.riccardo.giangiulio.services.ActivityTypeService;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class ActivityTypeController {
    private final ActivityTypeService activityTypeService;

    public ActivityTypeController(ActivityTypeService activityTypeService) {
        this.activityTypeService = activityTypeService;
    }

    public void registerRoutes(Javalin app) {
        app.post("/activityTypes", this::createActivityType);
        app.get("/activityTypes", this::getAllActivityTypes);
        app.get("/activityTypes/{activityTypeId}", this::getActivityTypeById);
        app.delete("/activityTypes/{activityTypeId}", this::deleteActivityType);
    }

    private void createActivityType(Context ctx) {
        try {
            ActivityType activityType = ctx.bodyAsClass(ActivityType.class);
            ActivityType createdActivityType = activityTypeService.createActivityType(activityType);
            ctx.status(201).json(createdActivityType);
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    private void getAllActivityTypes(Context ctx) {
        try {
            ctx.json(activityTypeService.getAllActivityTypes());
        } catch (RuntimeException e) {
            ctx.status(500).result(e.getMessage());
        }
    }

    private void getActivityTypeById(Context ctx) {
        try {
            long activityTypeId = Long.parseLong(ctx.pathParam("activityTypeId"));
            ActivityType activityType = activityTypeService.getActivityTypeById(activityTypeId);
            if (activityType != null) {
                ctx.json(activityType);
            } else {
                ctx.status(404).result("Activity type not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid activity type ID");
        }
    }

    private void deleteActivityType(Context ctx) {
        try {
            long activityTypeId = Long.parseLong(ctx.pathParam("activityTypeId"));
            activityTypeService.deleteActivityType(activityTypeId);
            ctx.status(204).result("Activity type deleted successfully");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid activity type ID");
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }
}
