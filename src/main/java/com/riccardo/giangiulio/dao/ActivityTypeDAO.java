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
        String insertActivityTypeSQL = "INSERT INTO public.\"ActivityType\"(name) VALUES (?)";

        try (PreparedStatement psInsertActivityType = connection.prepareStatement(insertActivityTypeSQL)) {
            psInsertActivityType.setString(1, activityType.getName());

            psInsertActivityType.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating activity type", e);
        }
        return null;
    }

    public ActivityType getActivityTypeById(long activityTypeId) {
        String getActivityTypeByIdSQL = "SELECT * FROM public.\"ActivityType\" WHERE activity_type_id = ?";

        try (PreparedStatement psGetActivityTypeById = connection.prepareStatement(getActivityTypeByIdSQL)) {
            psGetActivityTypeById.setLong(1, activityTypeId);
            ResultSet rs = psGetActivityTypeById.executeQuery();
            if(rs.next()) {
                ActivityType activityType = new ActivityType();
                activityType.setActivityTypeId(rs.getLong("activity_type_id"));
                activityType.setName(rs.getString("name"));
                return activityType;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving activity type", e);
        }
        return null;
    }

    public List<ActivityType> getAllActivityTypes() {
        String getAllActivityTypesSQL = "SELECT * FROM public.\"ActivityType\"";
        List<ActivityType> activityTypes = new ArrayList<>();

        try (PreparedStatement psGetAllActivityTypes = connection.prepareStatement(getAllActivityTypesSQL)) {
            ResultSet rs = psGetAllActivityTypes.executeQuery();

            while (rs.next()) {
                ActivityType activityType = new ActivityType();
                activityType.setActivityTypeId(rs.getLong("activity_type_id"));
                activityType.setName(rs.getString("name"));
                activityTypes.add(activityType);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving activity types", e);
        }

        return activityTypes;
    }

    public void deleteActivityType(long activityTypeId) {
        String deleteActivityTypeSQL = "DELETE FROM public.\"ActivityType\" WHERE activity_type_id = ?";

        try (PreparedStatement psDeleteActivityType = connection.prepareStatement(deleteActivityTypeSQL)) {
            psDeleteActivityType.setLong(1, activityTypeId);
            psDeleteActivityType.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting activity type", e);
        }
    }
} 