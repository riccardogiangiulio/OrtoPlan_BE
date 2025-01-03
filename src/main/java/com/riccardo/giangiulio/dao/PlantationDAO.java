package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.riccardo.giangiulio.models.Plant;
import com.riccardo.giangiulio.models.Plantation;
import com.riccardo.giangiulio.models.User;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class PlantationDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    private boolean userExists(long userId) {
        String sql = "SELECT COUNT(*) FROM public.\"User\" WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking user existence", e);
        }
        return false;
    }

    private boolean plantExists(long plantId) {
        String sql = "SELECT COUNT(*) FROM public.\"Plant\" WHERE plant_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, plantId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking plant existence", e);
        }
        return false;
    }

    public Plantation createPlantation(Plantation plantation) {
        // Verifica solo che gli ID esistano
        if (!userExists(plantation.getUser().getUserId())) {
            throw new RuntimeException("User with ID " + plantation.getUser().getUserId() + " not found");
        }
        if (!plantExists(plantation.getPlant().getPlantId())) {
            throw new RuntimeException("Plant with ID " + plantation.getPlant().getPlantId() + " not found");
        }

        String insertPlantationSQL = "INSERT INTO public.\"Plantation\"(name, city, start_date, end_date, user_id, plant_id) VALUES (?, ?, ?, ?, ?, ?) RETURNING plantation_id";

        try (PreparedStatement psInsertPlantation = connection.prepareStatement(insertPlantationSQL, Statement.RETURN_GENERATED_KEYS)) {
            psInsertPlantation.setString(1, plantation.getName());
            psInsertPlantation.setString(2, plantation.getCity());
            psInsertPlantation.setDate(3, Date.valueOf(plantation.getStartDate()));
            psInsertPlantation.setDate(4, Date.valueOf(plantation.getEndDate()));
            psInsertPlantation.setLong(5, plantation.getUser().getUserId());
            psInsertPlantation.setLong(6, plantation.getPlant().getPlantId());
            
            psInsertPlantation.executeUpdate();
            
            // Recupera l'ID generato
            ResultSet generatedKeys = psInsertPlantation.getGeneratedKeys();
            if (generatedKeys.next()) {
                plantation.setPlantationId(generatedKeys.getLong(1));
            }
            
            return plantation;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating plantation", e);
        }
    }

    public List<Plantation> getPlantationsOfUser(long userId) {
        List<Plantation> plantations = new ArrayList<>();
        String sql = "SELECT plantation_id, name, city, start_date, end_date, user_id, plant_id FROM public.\"Plantation\" WHERE user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Plantation plantation = new Plantation();
                plantation.setPlantationId(rs.getLong("plantation_id"));
                plantation.setName(rs.getString("name"));
                plantation.setCity(rs.getString("city"));
                plantation.setStartDate(rs.getDate("start_date").toLocalDate());
                plantation.setEndDate(rs.getDate("end_date").toLocalDate());
                
                // Imposta user con solo l'ID
                User user = new User();
                user.setUserId(rs.getLong("user_id"));
                plantation.setUser(user);
                
                // Imposta plant con solo l'ID
                Plant plant = new Plant();
                plant.setPlantId(rs.getLong("plant_id"));
                plantation.setPlant(plant);
                
                plantations.add(plantation);
            }
            
            return plantations;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving plantations", e);
        }
    }


    public Plantation getPlantationById(long plantationId) {
        String sql = "SELECT plantation_id, name, city, start_date, end_date, user_id, plant_id FROM public.\"Plantation\" WHERE plantation_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, plantationId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Plantation plantation = new Plantation();
                plantation.setPlantationId(rs.getLong("plantation_id"));
                plantation.setName(rs.getString("name"));
                plantation.setCity(rs.getString("city"));
                plantation.setStartDate(rs.getDate("start_date").toLocalDate());
                plantation.setEndDate(rs.getDate("end_date").toLocalDate());
                
                // Inizializza gli oggetti User e Plant prima di settare gli ID
                User user = new User();
                user.setUserId(rs.getLong("user_id"));
                plantation.setUser(user);
                
                Plant plant = new Plant();
                plant.setPlantId(rs.getLong("plant_id"));
                plantation.setPlant(plant);
                
                return plantation;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving plantation", e);
        }
    }

    public void updatePlantation(Plantation updates, long plantationId) {
        // Prima recupera la plantation esistente
        Plantation existing = getPlantationById(plantationId);
        if (existing == null) {
            throw new RuntimeException("Plantation not found");
        }

        // Usa i valori esistenti se non sono forniti negli updates
        String name = (updates.getName() != null) ? updates.getName() : existing.getName();
        String city = (updates.getCity() != null) ? updates.getCity() : existing.getCity();
        LocalDate startDate = (updates.getStartDate() != null) ? updates.getStartDate() : existing.getStartDate();
        LocalDate endDate = (updates.getEndDate() != null) ? updates.getEndDate() : existing.getEndDate();
        
        // Per user e plant, mantieni gli esistenti se non specificati
        long userId = (updates.getUser() != null && updates.getUser().getUserId() != 0) ? 
                     updates.getUser().getUserId() : existing.getUser().getUserId();
        long plantId = (updates.getPlant() != null && updates.getPlant().getPlantId() != 0) ? 
                      updates.getPlant().getPlantId() : existing.getPlant().getPlantId();

        String sql = "UPDATE public.\"Plantation\" SET name = ?, city = ?, start_date = ?, end_date = ?, user_id = ?, plant_id = ? WHERE plantation_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, city);
            ps.setDate(3, Date.valueOf(startDate));
            ps.setDate(4, Date.valueOf(endDate));
            ps.setLong(5, userId);
            ps.setLong(6, plantId);
            ps.setLong(7, plantationId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No rows were updated");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating plantation", e);
        }
    }

    public void deletePlantation(long plantationId, long userId) {
        String sql = "DELETE FROM public.\"Plantation\" WHERE plantation_id = ? AND user_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, plantationId);
            ps.setLong(2, userId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Plantation not found or unauthorized");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting plantation", e);
        }
    }
} 