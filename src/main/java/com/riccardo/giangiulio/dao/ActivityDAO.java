package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.riccardo.giangiulio.models.Activity;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class ActivityDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Activity createActivity(Activity activity) {
        String insertActivitySQL = "INSERT INTO public.\"Activity\"(description, scheduled_dt, completed, activity_type_id, plantation_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement psInsertActivity = connection.prepareStatement(insertActivitySQL)) {
            psInsertActivity.setString(1, activity.getDescription());
            psInsertActivity.setTimestamp(2, Timestamp.valueOf(activity.getScheduled_dt()));
            psInsertActivity.setBoolean(3, activity.isCompleted());
            psInsertActivity.setLong(4, activity.getActivityType().getActivityTypeId());
            psInsertActivity.setLong(5, activity.getPlantation().getPlantationId());

            psInsertActivity.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la creazione dell'attività", e);
        }
        return null;
    }

    public Activity getActivityById(long activityId) {
        String getActivityByIdSQL = "SELECT * FROM public.\"Activity\" WHERE activity_id = ?";

        try (PreparedStatement psSelectActivityById = connection.prepareStatement(getActivityByIdSQL)) {
            psSelectActivityById.setLong(1, activityId);
            ResultSet rs = psSelectActivityById.executeQuery();
            if(rs.next()) {
                Activity activity = new Activity();
                activity.setActivityId(rs.getLong("activity_id"));
                activity.setDescription(rs.getString("description"));
                activity.setScheduled_dt(rs.getTimestamp("scheduled_dt").toLocalDateTime());
                activity.setCompleted(rs.getBoolean("completed"));
                return activity;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero dell'attività", e);
        }
        return null;
    }

    public List<Activity> getActivitiesByPlantationId(long plantationId) {
        String getActivitiesByPlantationIdSQL = "SELECT * FROM public.\"Activity\" WHERE plantation_id = ? ORDER BY scheduled_dt";
        List<Activity> activities = new ArrayList<>();

        try (PreparedStatement psSelectActivities = connection.prepareStatement(getActivitiesByPlantationIdSQL)) {
            psSelectActivities.setLong(1, plantationId);
            ResultSet rs = psSelectActivities.executeQuery();

            while (rs.next()) {
                Activity activity = new Activity();
                activity.setActivityId(rs.getLong("activity_id"));
                activity.setDescription(rs.getString("description"));
                activity.setScheduled_dt(rs.getTimestamp("scheduled_dt").toLocalDateTime());
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
                activity.setScheduled_dt(rs.getTimestamp("scheduled_dt").toLocalDateTime());
                activity.setCompleted(rs.getBoolean("completed"));
                activities.add(activity);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero delle attività pendenti", e);
        }
        return activities;
    }

    public void updateActivity(Activity activity) {
        String updateSQL = "UPDATE public.\"Activity\" SET description = ?, scheduled_dt = ?, completed = ? WHERE activity_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(updateSQL)) {
            ps.setString(1, activity.getDescription());
            ps.setTimestamp(2, Timestamp.valueOf(activity.getScheduled_dt()));
            ps.setBoolean(3, activity.isCompleted());
            ps.setLong(4, activity.getActivityId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Attività non trovata");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento dell'attività", e);
        }
    }

    public void deleteActivity(long activityId) {
        String deleteActivitySQL = "DELETE FROM public.\"Activity\" WHERE activity_id = ?";

        try (PreparedStatement psDeleteActivity = connection.prepareStatement(deleteActivitySQL)) {
            psDeleteActivity.setLong(1, activityId);
            psDeleteActivity.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la cancellazione dell'attività", e);
        }
    }
} 