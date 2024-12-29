package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.riccardo.giangiulio.models.Activity;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class ActivityDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public void createActivity(Activity activity) {
        String insertSQL = "INSERT INTO public.\"Activity\"(description, scheduled_dt, completed, activity_type_id, plantation_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setString(1, activity.getDescription());
            ps.setObject(2, activity.getScheduled_dt());
            ps.setBoolean(3, activity.isCompleted());
            ps.setLong(4, activity.getActivityType().getActivityTypeId());
            ps.setLong(5, activity.getPlantation().getPlantationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la creazione dell'attività", e);
        }
    }

    public List<Activity> getActivitiesByPlantationId(long plantationId) {
        String selectSQL = "SELECT * FROM public.\"Activity\" WHERE plantation_id = ? ORDER BY scheduled_dt";
        List<Activity> activities = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setLong(1, plantationId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Activity activity = new Activity();
                activity.setActivityId(rs.getLong("activity_id"));
                activity.setDescription(rs.getString("description"));
                activity.setScheduled_dt(rs.getObject("scheduled_dt", LocalDateTime.class));
                activity.setCompleted(rs.getBoolean("completed"));
                activities.add(activity);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero delle attività", e);
        }

        return activities;
    }

    public List<Activity> getPendingActivities(long plantationId) {
        String selectSQL = "SELECT * FROM public.\"Activity\" WHERE plantation_id = ? AND completed = false AND scheduled_dt >= CURRENT_TIMESTAMP ORDER BY scheduled_dt";
        List<Activity> activities = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setLong(1, plantationId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Activity activity = new Activity();
                activity.setActivityId(rs.getLong("activity_id"));
                activity.setDescription(rs.getString("description"));
                activity.setScheduled_dt(rs.getObject("scheduled_dt", LocalDateTime.class));
                activity.setCompleted(rs.getBoolean("completed"));
                activities.add(activity);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero delle attività pendenti", e);
        }

        return activities;
    }

    public void markActivityAsCompleted(long activityId) {
        String updateSQL = "UPDATE public.\"Activity\" SET completed = true WHERE activity_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(updateSQL)) {
            ps.setLong(1, activityId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Attività non trovata");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento dell'attività", e);
        }
    }

    public void deleteActivity(long activityId) {
        String deleteSQL = "DELETE FROM public.\"Activity\" WHERE activity_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setLong(1, activityId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Attività non trovata");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la cancellazione dell'attività", e);
        }
    }
} 