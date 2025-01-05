package com.riccardo.giangiulio.controller;

import com.riccardo.giangiulio.models.Plantation;
import com.riccardo.giangiulio.services.PlantationService;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class PlantationController {
    private final PlantationService plantationService;

    public PlantationController(PlantationService plantationService) {
        this.plantationService = plantationService;
    }

    public void registerRoutes(Javalin app) {
        app.post("/plantations", this::createPlantation);
        app.get("/plantations/user/{userId}", this::getPlantationsOfUser);
        app.get("/plantations/{plantationId}", this::getPlantationById);
        app.put("/plantations/{plantationId}", this::updatePlantation);
        app.delete("/plantations/{plantationId}", this::deletePlantation);
    }

    private void createPlantation(Context ctx) {
        try {
            Plantation plantation = ctx.bodyAsClass(Plantation.class);
            Plantation createdPlantation = plantationService.createPlantation(plantation);
            ctx.status(201).json(createdPlantation);
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    private void getPlantationsOfUser(Context ctx) {
        try {
            long userId = Long.parseLong(ctx.pathParam("userId"));
            ctx.json(plantationService.getPlantationsOfUser(userId));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID");
        } catch (RuntimeException e) {
            ctx.status(500).result(e.getMessage());
        }
    }

    private void getPlantationById(Context ctx) {
        try {
            long plantationId = Long.parseLong(ctx.pathParam("plantationId"));
            Plantation plantation = plantationService.getPlantationById(plantationId);
            if (plantation != null) {
                ctx.json(plantation);
            } else {
                ctx.status(404).result("Plantation not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid plantation ID");
        }
    }

    private void updatePlantation(Context ctx) {
        try {
            long plantationId = Long.parseLong(ctx.pathParam("plantationId"));
            Plantation updates = ctx.bodyAsClass(Plantation.class);
            
            plantationService.updatePlantation(updates, plantationId);
            ctx.json(plantationService.getPlantationById(plantationId));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid plantation ID");
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    private void deletePlantation(Context ctx) {
        try {
            long plantationId = Long.parseLong(ctx.pathParam("plantationId"));
            
            Plantation plantation = plantationService.getPlantationById(plantationId);
            if (plantation == null) {
                ctx.status(404).result("Plantation not found");
                return;
            }

            long userId = plantation.getUser().getUserId();
            
            plantationService.deletePlantation(plantationId, userId);
            ctx.status(204).result("Plantation deleted successfully");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid plantation ID");
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }
} 