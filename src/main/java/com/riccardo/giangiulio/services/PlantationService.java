package com.riccardo.giangiulio.services;

import java.util.List;

import com.riccardo.giangiulio.dao.PlantationDAO;
import com.riccardo.giangiulio.models.Plantation;

public class PlantationService {
    private final PlantationDAO plantationDAO = new PlantationDAO();

    public Plantation createPlantation(Plantation plantation) {
        return plantationDAO.createPlantation(plantation);
    }

    public List<Plantation> getPlantationsOfUser(long userId) {
        return plantationDAO.getPlantationsOfUser(userId);
    }

    public Plantation getPlantationById(long plantationId) {
        return plantationDAO.getPlantationById(plantationId);
    }

    public void updatePlantation(Plantation updates, long plantationId) {
        plantationDAO.updatePlantation(updates, plantationId);
    }

    public void deletePlantation(long plantationId, long userId) {
        plantationDAO.deletePlantation(plantationId, userId);
    }
}