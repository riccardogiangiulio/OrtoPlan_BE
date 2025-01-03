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

    public ActivityType createActivityType(ActivityType activityType) {
        String sql = "INSERT INTO public.\"ActivityType\"(name) VALUES (?) RETURNING type_id";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, activityType.getName());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                activityType.setActivityTypeId(rs.getLong("type_id"));
                return activityType;
            }

            throw new RuntimeException("Failed to retrieve generated ID");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating activity type: " + e.getMessage(), e);
        }
    }   

    public ActivityType getActivityTypeById(long activityTypeId) {
        String sql = "SELECT type_id, name FROM public.\"ActivityType\" WHERE type_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, activityTypeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ActivityType activityType = new ActivityType();
                activityType.setActivityTypeId(rs.getLong("type_id"));
                activityType.setName(rs.getString("name"));
                return activityType;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving activity type", e);
        }
        return null;
    }

    public List<ActivityType> getAllActivityTypes() {
        String sql = "SELECT type_id, name FROM public.\"ActivityType\"";
        List<ActivityType> activityTypes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ActivityType activityType = new ActivityType();
                activityType.setActivityTypeId(rs.getLong("type_id"));
                activityType.setName(rs.getString("name"));
                activityTypes.add(activityType);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving activity types", e);
        }
        return activityTypes;
    }

    public void deleteActivityType(long activityTypeId) {
        String sql = "DELETE FROM public.\"ActivityType\" WHERE type_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, activityTypeId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Activity type not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting activity type", e);
        }
    }
}