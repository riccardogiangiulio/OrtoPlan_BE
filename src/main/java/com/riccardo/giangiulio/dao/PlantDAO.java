package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.time.LocalDate;

import com.riccardo.giangiulio.models.Plant;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class PlantDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Plant createPlant(Plant plant) {
        String insertPlantSQL = "INSERT INTO public.\"Plant\"(name, description, cultivation_start, cultivation_end, harvest_time) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement psInsertPlant = connection.prepareStatement(insertPlantSQL)) {
            psInsertPlant.setString(1, plant.getName());
            psInsertPlant.setString(2, plant.getDescription());
            psInsertPlant.setDate(3, Date.valueOf(plant.getCultivationStart()));
            psInsertPlant.setDate(4, Date.valueOf(plant.getCultivationEnd()));
            psInsertPlant.setInt(5, plant.getHarvestTime());

            psInsertPlant.executeUpdate();  
        } catch (SQLException e) {
            throw new RuntimeException("Error creating plant", e);
        }
        return null;
    }

    public Plant getPlantById(long plantId) {
        String getPlantByIdSQL = "SELECT * FROM public.\"Plant\" WHERE plant_id = ?";

        try (PreparedStatement psSelect = connection.prepareStatement(getPlantByIdSQL)) {
            psSelect.setLong(1, plantId);
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                Plant plant = new Plant();
                plant.setPlantId(rs.getLong("plant_id"));
                plant.setName(rs.getString("name"));
                plant.setDescription(rs.getString("description"));
                plant.setCultivationStart(rs.getDate("cultivation_start").toLocalDate());
                plant.setCultivationEnd(rs.getDate("cultivation_end").toLocalDate());
                plant.setHarvestTime(rs.getInt("harvest_time"));
                return plant;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving plant", e);
        }
        return null;
    }

    public List<Plant> getAllPlants() {
        String getAllPlantsBySQL = "SELECT * FROM public.\"Plant\"";
        List<Plant> plants = new ArrayList<>();

        try (PreparedStatement psSelectAll = connection.prepareStatement(getAllPlantsBySQL)) {
            ResultSet rs = psSelectAll.executeQuery();

            while (rs.next()) {
                Plant plant = new Plant();
                plant.setPlantId(rs.getLong("plant_id"));
                plant.setName(rs.getString("name"));
                plant.setDescription(rs.getString("description"));
                plant.setCultivationStart(rs.getDate("cultivation_start").toLocalDate());
                plant.setCultivationEnd(rs.getDate("cultivation_end").toLocalDate());
                plant.setHarvestTime(rs.getInt("harvest_time"));
                plants.add(plant);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving plants", e);
        }
        return plants;
    }

    public void updatePlant(Plant updates, long plantId) {
        // Prima ottieni la pianta esistente
        Plant existingPlant = getPlantById(plantId);
        if (existingPlant == null) {
            throw new RuntimeException("Plant not found");
        }

        // Prepara i valori da aggiornare, usando i valori esistenti se non specificati
        String name = (updates.getName() == null || updates.getName().isEmpty()) ? existingPlant.getName() : updates.getName();
        String description = (updates.getDescription() == null) ? existingPlant.getDescription() : updates.getDescription();
        LocalDate cultivationStart = (updates.getCultivationStart() == null) ? existingPlant.getCultivationStart() : updates.getCultivationStart();
        LocalDate cultivationEnd = (updates.getCultivationEnd() == null) ? existingPlant.getCultivationEnd() : updates.getCultivationEnd();
        int harvestTime = (updates.getHarvestTime() == 0) ? existingPlant.getHarvestTime() : updates.getHarvestTime();

        String updatePlantSQL = "UPDATE public.\"Plant\" SET name = ?, description = ?, cultivation_start = ?, cultivation_end = ?, harvest_time = ? WHERE plant_id = ?";

        try (PreparedStatement psUpdatePlant = connection.prepareStatement(updatePlantSQL)) {
            psUpdatePlant.setString(1, name);
            psUpdatePlant.setString(2, description);
            psUpdatePlant.setDate(3, Date.valueOf(cultivationStart));
            psUpdatePlant.setDate(4, Date.valueOf(cultivationEnd));
            psUpdatePlant.setInt(5, harvestTime);
            psUpdatePlant.setLong(6, plantId);

            psUpdatePlant.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating plant", e);
        }
    }

    public void deletePlant(long plantId) {
        String deletePlantSQL = "DELETE FROM public.\"Plant\" WHERE plant_id = ?";

        try (PreparedStatement psDeletePlant = connection.prepareStatement(deletePlantSQL)) {
            psDeletePlant.setLong(1, plantId);
            psDeletePlant.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting plant", e);
        }
    }
} 