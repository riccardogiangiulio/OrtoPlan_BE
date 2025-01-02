package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.riccardo.giangiulio.models.Plantation;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class PlantationDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Plantation createPlantation(Plantation plantation) {
        String insertPlantationSQL = "INSERT INTO public.\"Plantation\"(name, city, start_date, end_date, user_id, plant_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement psInsertPlantation = connection.prepareStatement(insertPlantationSQL)) {
            psInsertPlantation.setString(1, plantation.getName());
            psInsertPlantation.setString(2, plantation.getCity());
            psInsertPlantation.setDate(3, Date.valueOf(plantation.getStartDate()));
            psInsertPlantation.setDate(4, Date.valueOf(plantation.getEndDate()));
            psInsertPlantation.setLong(5, plantation.getUser().getUserId());
            psInsertPlantation.setLong(6, plantation.getPlant().getPlantId());
            
            psInsertPlantation.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating plantation", e);
        }
        return null;
    }

    public List<Plantation> getPlantationsOfUser(long userId) {
        String getAllPlantationsOfUserBySQL = "SELECT * FROM public.\"Plantation\" WHERE user_id = ?";
        List<Plantation> plantations = new ArrayList<>();

        try (PreparedStatement psSelectAll = connection.prepareStatement(getAllPlantationsOfUserBySQL)) {
            ResultSet rs = psSelectAll.executeQuery();

            while (rs.next()) {
                Plantation plantation = new Plantation();
                plantation.setPlantationId(rs.getLong("plantation_id"));
                plantation.setName(rs.getString("name"));
                plantation.setCity(rs.getString("city"));
                plantation.setStartDate(rs.getDate("start_date").toLocalDate());
                plantation.setEndDate(rs.getDate("end_date").toLocalDate());
                plantation.getUser().setUserId(rs.getLong("user_id"));
                plantation.getPlant().setPlantId(rs.getLong("plant_id"));
                plantations.add(plantation);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving plantations", e);
        }
        return plantations;
    }


    public Plantation getPlantationById(long plantationId) {
        String getPlantationByIdSQL = "SELECT * FROM public.\"Plantation\" WHERE plantation_id = ?";

        try (PreparedStatement psSelect = connection.prepareStatement(getPlantationByIdSQL)) {
            psSelect.setLong(1, plantationId);
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                Plantation plantation = new Plantation();
                plantation.setPlantationId(rs.getLong("plantation_id"));
                plantation.setName(rs.getString("name"));
                plantation.setCity(rs.getString("city"));
                plantation.setStartDate(rs.getDate("start_date").toLocalDate());
                plantation.setEndDate(rs.getDate("end_date").toLocalDate());
                plantation.getUser().setUserId(rs.getLong("user_id"));
                plantation.getPlant().setPlantId(rs.getLong("plant_id"));
                return plantation;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving plantation", e);
        }
        return null;
    }

    public void updatePlantation(Plantation plantation) {
        String updatePlantationSQL = "UPDATE public.\"Plantation\" SET name = ?, city = ?, start_date = ?, end_date = ? WHERE plantation_id = ? AND user_id = ?";

        try (PreparedStatement psUpdatePlantation = connection.prepareStatement(updatePlantationSQL)) {
            psUpdatePlantation.setString(1, plantation.getName());
            psUpdatePlantation.setString(2, plantation.getCity());
            psUpdatePlantation.setObject(3, plantation.getStartDate());
            psUpdatePlantation.setObject(4, plantation.getEndDate());
            psUpdatePlantation.setLong(5, plantation.getPlantationId());
            psUpdatePlantation.setLong(6, plantation.getUser().getUserId());

            int rowsAffected = psUpdatePlantation.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Plantation not found or unauthorized");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating plantation", e);
        }
    }

    public void deletePlantation(long plantationId, long userId) {
        String deletePlantationSQL = "DELETE FROM public.\"Plantation\" WHERE plantation_id = ? AND user_id = ?";

        try (PreparedStatement psDeletePlantation = connection.prepareStatement(deletePlantationSQL)) {
            psDeletePlantation.setLong(1, plantationId);
            psDeletePlantation.setLong(2, userId);

            psDeletePlantation.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting plantation", e);
        }
    }
} 