package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.riccardo.giangiulio.models.Plant;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class PlantDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public void createPlant(Plant plant) {
        String insertSQL = "INSERT INTO public.\"Plant\"(name, description, cultivation_start, cultivation_end, harvest_time) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setString(1, plant.getName());
            ps.setString(2, plant.getDescription());
            ps.setObject(3, plant.getCultivationStart());
            ps.setObject(4, plant.getCultivationEnd());
            ps.setInt(5, plant.getHarvestTime());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la creazione della pianta", e);
        }
    }

    public List<Plant> getAllPlants() {
        String selectSQL = "SELECT * FROM public.\"Plant\"";
        List<Plant> plants = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                plants.add(new Plant(
                    rs.getLong("plant_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getObject("cultivation_start", LocalDate.class),
                    rs.getObject("cultivation_end", LocalDate.class),
                    rs.getInt("harvest_time")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero delle piante", e);
        }

        return plants;
    }

    public Plant getPlantById(long plantId) {
        String selectSQL = "SELECT * FROM public.\"Plant\" WHERE plant_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setLong(1, plantId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Pianta non trovata");
            }

            return new Plant(
                plantId,
                rs.getString("name"),
                rs.getString("description"),
                rs.getObject("cultivation_start", LocalDate.class),
                rs.getObject("cultivation_end", LocalDate.class),
                rs.getInt("harvest_time")
            );

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero della pianta", e);
        }
    }

    public void updatePlant(Plant plant) {
        String updateSQL = "UPDATE public.\"Plant\" SET name = ?, description = ?, cultivation_start = ?, cultivation_end = ?, harvest_time = ? WHERE plant_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(updateSQL)) {
            ps.setString(1, plant.getName());
            ps.setString(2, plant.getDescription());
            ps.setObject(3, plant.getCultivationStart());
            ps.setObject(4, plant.getCultivationEnd());
            ps.setInt(5, plant.getHarvestTime());
            ps.setLong(6, plant.getPlantId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Pianta non trovata");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento della pianta", e);
        }
    }

    public void deletePlant(long plantId) {
        String deleteSQL = "DELETE FROM public.\"Plant\" WHERE plant_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setLong(1, plantId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Pianta non trovata");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la cancellazione della pianta", e);
        }
    }

    public List<Plant> getPlantsByPeriod(LocalDate date) {
        String selectSQL = "SELECT * FROM public.\"Plant\" WHERE cultivation_start <= ? AND cultivation_end >= ?";
        List<Plant> plants = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, date);
            ps.setObject(2, date);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                plants.add(new Plant(
                    rs.getLong("plant_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getObject("cultivation_start", LocalDate.class),
                    rs.getObject("cultivation_end", LocalDate.class),
                    rs.getInt("harvest_time")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero delle piante per periodo", e);
        }

        return plants;
    }
} 