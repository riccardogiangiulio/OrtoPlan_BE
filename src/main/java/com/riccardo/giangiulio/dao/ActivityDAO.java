package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.riccardo.giangiulio.models.Activity;
import com.riccardo.giangiulio.models.ActivityType;
import com.riccardo.giangiulio.models.Plantation;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class ActivityDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Activity createActivity(Activity activity) {
        String sql = "INSERT INTO public.\"Activity\" (description, scheduled_dt, completed, type_id, plantation_id) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING activity_id";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, activity.getDescription());
            ps.setTimestamp(2, Timestamp.valueOf(activity.getScheduled_dt()));
            ps.setBoolean(3, activity.isCompleted());
            ps.setLong(4, activity.getActivityType().getActivityTypeId());
            ps.setLong(5, activity.getPlantation().getPlantationId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                activity.setActivityId(rs.getLong("activity_id"));
                return activity;
            }
            throw new RuntimeException("Failed to retrieve generated ID");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating activity: " + e.getMessage());
        }
    }

    public Activity getActivityById(long activityId) {
        String sql = "SELECT a.activity_id, a.description, a.scheduled_dt, a.completed, " +
                    "t.type_id, t.name as type_name, " +
                    "p.plantation_id, p.name as plantation_name " +
                    "FROM public.\"Activity\" a, public.\"ActivityType\" t, public.\"Plantation\" p " +
                    "WHERE a.type_id = t.type_id AND a.plantation_id = p.plantation_id AND a.activity_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, activityId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Activity activity = new Activity();
                activity.setActivityId(rs.getLong("activity_id"));
                activity.setDescription(rs.getString("description"));
                activity.setScheduled_dt(rs.getTimestamp("scheduled_dt").toLocalDateTime());
                activity.setCompleted(rs.getBoolean("completed"));
                
                ActivityType activityType = new ActivityType();
                activityType.setActivityTypeId(rs.getLong("type_id"));
                activityType.setName(rs.getString("type_name"));
                activity.setActivityType(activityType);
                
                Plantation plantation = new Plantation();
                plantation.setPlantationId(rs.getLong("plantation_id"));
                plantation.setName(rs.getString("plantation_name"));
                activity.setPlantation(plantation);
                
                return activity;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving activity", e);
        }
        return null;
    }

    public List<Activity> getActivitiesByPlantationId(long plantationId) {
        String sql = "SELECT a.activity_id, a.description, a.scheduled_dt, a.completed, " +
                    "t.type_id, t.name as type_name, " +
                    "p.plantation_id, p.name as plantation_name " +
                    "FROM public.\"Activity\" a, public.\"ActivityType\" t, public.\"Plantation\" p " +
                    "WHERE a.type_id = t.type_id AND a.plantation_id = p.plantation_id " +
                    "AND a.plantation_id = ? ORDER BY a.scheduled_dt";

        List<Activity> activities = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, plantationId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Activity activity = new Activity();
                activity.setActivityId(rs.getLong("activity_id"));
                activity.setDescription(rs.getString("description"));
                activity.setScheduled_dt(rs.getTimestamp("scheduled_dt").toLocalDateTime());
                activity.setCompleted(rs.getBoolean("completed"));
                
                ActivityType activityType = new ActivityType();
                activityType.setActivityTypeId(rs.getLong("type_id"));
                activityType.setName(rs.getString("type_name"));
                activity.setActivityType(activityType);
                
                Plantation plantation = new Plantation();
                plantation.setPlantationId(rs.getLong("plantation_id"));
                plantation.setName(rs.getString("plantation_name"));
                activity.setPlantation(plantation);
                
                activities.add(activity);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving activities", e);
        }
        return activities;
    }

    public List<Activity> getPendingActivities(long plantationId) {
        String sql = "SELECT a.activity_id, a.description, a.scheduled_dt, a.completed, " +
                    "t.type_id, t.name as type_name, " +
                    "p.plantation_id, p.name as plantation_name " +
                    "FROM public.\"Activity\" a, public.\"ActivityType\" t, public.\"Plantation\" p " +
                    "WHERE a.type_id = t.type_id AND a.plantation_id = p.plantation_id " +
                    "AND a.plantation_id = ? AND a.completed = false " +
                    "ORDER BY a.scheduled_dt";

        List<Activity> activities = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, plantationId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Activity activity = new Activity();
                activity.setActivityId(rs.getLong("activity_id"));
                activity.setDescription(rs.getString("description"));
                activity.setScheduled_dt(rs.getTimestamp("scheduled_dt").toLocalDateTime());
                activity.setCompleted(rs.getBoolean("completed"));
                
                ActivityType activityType = new ActivityType();
                activityType.setActivityTypeId(rs.getLong("type_id"));
                activityType.setName(rs.getString("type_name"));
                activity.setActivityType(activityType);
                
                Plantation plantation = new Plantation();
                plantation.setPlantationId(rs.getLong("plantation_id"));
                plantation.setName(rs.getString("plantation_name"));
                activity.setPlantation(plantation);
                
                activities.add(activity);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving pending activities", e);
        }
        return activities;
    }

    public void updateActivity(Activity updatedActivity) {
        // Prima recupera l'attività esistente
        Activity existingActivity = getActivityById(updatedActivity.getActivityId());
        if (existingActivity == null) {
            throw new RuntimeException("Activity not found");
        }

        // Aggiorna solo i campi non null
        if (updatedActivity.getDescription() != null) {
            existingActivity.setDescription(updatedActivity.getDescription());
        }
        if (updatedActivity.getScheduled_dt() != null) {
            existingActivity.setScheduled_dt(updatedActivity.getScheduled_dt());
        }
        
        // Per il boolean completed, controlliamo se è presente nel payload
        if (updatedActivity.isCompleted() != existingActivity.isCompleted()) {
            existingActivity.setCompleted(updatedActivity.isCompleted());
        }

        // Esegue l'update con i valori aggiornati
        String sql = "UPDATE public.\"Activity\" SET description = ?, scheduled_dt = ?, completed = ? " +
                    "WHERE activity_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, existingActivity.getDescription());
            ps.setTimestamp(2, Timestamp.valueOf(existingActivity.getScheduled_dt()));
            ps.setBoolean(3, existingActivity.isCompleted());
            ps.setLong(4, existingActivity.getActivityId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating activity", e);
        }
    }

    public void deleteActivity(long activityId) {
        String sql = "DELETE FROM public.\"Activity\" WHERE activity_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, activityId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Activity not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting activity", e);
        }
    }
} 