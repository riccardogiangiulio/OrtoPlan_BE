package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.riccardo.giangiulio.models.ActivityType;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class ActivityTypeDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public void createActivityType(ActivityType activityType) {
        String insertSQL = "INSERT INTO public.\"ActivityType\"(name) VALUES (?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setString(1, activityType.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la creazione del tipo di attività", e);
        }
    }

    public List<ActivityType> getAllActivityTypes() {
        String selectSQL = "SELECT * FROM public.\"ActivityType\"";
        List<ActivityType> activityTypes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                activityTypes.add(new ActivityType(
                    rs.getLong("activity_type_id"),
                    rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero dei tipi di attività", e);
        }

        return activityTypes;
    }

    public ActivityType getActivityTypeById(long activityTypeId) {
        String selectSQL = "SELECT * FROM public.\"ActivityType\" WHERE activity_type_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setLong(1, activityTypeId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Tipo di attività non trovato");
            }

            return new ActivityType(
                activityTypeId,
                rs.getString("name")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero del tipo di attività", e);
        }
    }

    public void deleteActivityType(long activityTypeId) {
        String deleteSQL = "DELETE FROM public.\"ActivityType\" WHERE activity_type_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setLong(1, activityTypeId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Tipo di attività non trovato");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la cancellazione del tipo di attività", e);
        }
    }
} 