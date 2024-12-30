package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.riccardo.giangiulio.models.Plant;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class PlantDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Plant createPlant(Plant plant) {
        String insertPlantSQL = "INSERT INTO public.\"Plant\"(name, description, cultivation_start, cultivation_end, harvest_time) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement psInsertPlant = connection.prepareStatement(insertPlantSQL)) {
            psInsertPlant.setString(1, plant.getName());
            psInsertPlant.setString(2, plant.getDescription());
            psInsertPlant.setDate(3, java.sql.Date.valueOf(plant.getCultivationStart()));
            psInsertPlant.setDate(4, java.sql.Date.valueOf(plant.getCultivationEnd()));
            psInsertPlant.setInt(5, plant.getHarvestTime());

            psInsertPlant.executeQuery();  
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la creazione della pianta", e);
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
            throw new RuntimeException("Errore durante il recupero della pianta", e);
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
            throw new RuntimeException("Errore durante il recupero delle piante", e);
        }
        return plants;
    }

    public void updatePlant(Plant plant) {
        String updatePlantSQL = "UPDATE public.\"Plant\" SET name = ?, description = ?, cultivation_start = ?, cultivation_end = ?, harvest_time = ? WHERE plant_id = ?";

        try (PreparedStatement psUpdatePlant = connection.prepareStatement(updatePlantSQL)) {
            psUpdatePlant.setString(1, plant.getName());
            psUpdatePlant.setString(2, plant.getDescription());
            psUpdatePlant.setDate(3, java.sql.Date.valueOf(plant.getCultivationStart()));
            psUpdatePlant.setDate(4, java.sql.Date.valueOf(plant.getCultivationEnd()));
            psUpdatePlant.setInt(5, plant.getHarvestTime());
            psUpdatePlant.setLong(6, plant.getPlantId());

            psUpdatePlant.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento della pianta", e);
        }
    }

    public void deletePlant(long plantId) {
        String deletePlantSQL = "DELETE FROM public.\"Plant\" WHERE plant_id = ?";

        try (PreparedStatement psDeletePlant = connection.prepareStatement(deletePlantSQL)) {
            psDeletePlant.setLong(1, plantId);
            psDeletePlant.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la cancellazione della pianta", e);
        }
    }
} 