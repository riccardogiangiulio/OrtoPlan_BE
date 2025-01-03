package com.riccardo.giangiulio.controller;

import com.riccardo.giangiulio.models.Activity;
import com.riccardo.giangiulio.services.ActivityService;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    public void registerRoutes(Javalin app) {
        app.post("/activities", this::createActivity);
        app.get("/activities/{activityId}", this::getActivityById);
        app.get("/activities/plantation/{plantationId}", this::getActivitiesByPlantationId);
        app.get("/activities/plantation/{plantationId}/pending", this::getPendingActivities);
        app.put("/activities/{activityId}", this::updateActivity);
        app.delete("/activities/{activityId}", this::deleteActivity);
    }

    private void createActivity(Context ctx) {
        try {
            Activity activity = ctx.bodyAsClass(Activity.class);
            Activity createdActivity = activityService.createActivity(activity);
            ctx.status(201).json(createdActivity);
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    private void getActivityById(Context ctx) {
        try {
            long activityId = Long.parseLong(ctx.pathParam("activityId"));
            Activity activity = activityService.getActivityById(activityId);
            if (activity != null) {
                ctx.json(activity);
            } else {
                ctx.status(404).result("Activity not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid activity ID");
        }
    }

    private void getActivitiesByPlantationId(Context ctx) {
        try {
            long plantationId = Long.parseLong(ctx.pathParam("plantationId"));
            ctx.json(activityService.getActivitiesByPlantationId(plantationId));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid plantation ID");
        } catch (RuntimeException e) {
            ctx.status(500).result(e.getMessage());
        }
    }

    private void getPendingActivities(Context ctx) {
        try {
            long plantationId = Long.parseLong(ctx.pathParam("plantationId"));
            ctx.json(activityService.getPendingActivities(plantationId));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid plantation ID");
        } catch (RuntimeException e) {
            ctx.status(500).result(e.getMessage());
        }
    }

    private void updateActivity(Context ctx) {
        long activityId = Long.parseLong(ctx.pathParam("activityId"));
        Activity activity = ctx.bodyAsClass(Activity.class);
        Activity updatedActivity = activityService.updateActivity(activityId, activity);
        ctx.json(updatedActivity);
    }

    private void deleteActivity(Context ctx) {
        try {
            long activityId = Long.parseLong(ctx.pathParam("activityId"));
            activityService.deleteActivity(activityId);
            ctx.status(204).result("Activity deleted successfully");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid activity ID");
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }
}
