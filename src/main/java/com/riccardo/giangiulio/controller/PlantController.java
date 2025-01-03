package com.riccardo.giangiulio.controller;

import com.riccardo.giangiulio.models.Plant;
import com.riccardo.giangiulio.services.PlantService;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class PlantController {
    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    public void registerRoutes(Javalin app) {
        app.post("/plants", this::createPlant);
        app.get("/plants", this::getAllPlants);
        app.get("/plants/{plantId}", this::getPlantById);
        app.put("/plants/{plantId}", this::updatePlant);
        app.delete("/plants/{plantId}", this::deletePlant);
    }

    private void createPlant(Context ctx) {
        try {
            Plant plant = ctx.bodyAsClass(Plant.class);
            plantService.createPlant(plant);
            ctx.status(201).json(plant);
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    private void getAllPlants(Context ctx) {
        try {
            ctx.json(plantService.getAllPlants());
        } catch (RuntimeException e) {
            ctx.status(500).result(e.getMessage());
        }
    }

    private void getPlantById(Context ctx) {
        try {
            long plantId = Long.parseLong(ctx.pathParam("plantId"));
            Plant plant = plantService.getPlantById(plantId);
            if (plant != null) {
                ctx.json(plant);
            } else {
                ctx.status(404).result("Plant not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid plant ID");
        }
    }

    private void updatePlant(Context ctx) {
        try {
            long plantId = Long.parseLong(ctx.pathParam("plantId"));
            Plant updates = ctx.bodyAsClass(Plant.class);
            plantService.updatePlant(updates, plantId);
            ctx.json(plantService.getPlantById(plantId));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid plant ID");
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    private void deletePlant(Context ctx) {
        try {
            long plantId = Long.parseLong(ctx.pathParam("plantId"));
            plantService.removePlant(plantId);
            ctx.status(204).result("Plant deleted successfully");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid plant ID");
        }
    }
}
