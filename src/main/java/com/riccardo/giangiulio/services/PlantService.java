package com.riccardo.giangiulio.services;

import java.util.List;

import com.riccardo.giangiulio.dao.PlantDAO;
import com.riccardo.giangiulio.models.Plant;

public class PlantService {
    private final PlantDAO plantDAO = new PlantDAO();

    public void createPlant(Plant plant) {
        plantDAO.createPlant(plant);
    }

    public Plant getPlantById(long plantId) {
        return plantDAO.getPlantById(plantId);
    }

    public List<Plant> getAllPlants() {
        return plantDAO.getAllPlants();
    }

    public void updatePlant(Plant updates, long plantId) {
        plantDAO.updatePlant(updates, plantId);
    }

    public void removePlant(long plantId) {
        plantDAO.deletePlant(plantId);
    }
} 