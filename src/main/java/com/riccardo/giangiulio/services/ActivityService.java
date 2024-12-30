package com.riccardo.giangiulio.services;

import java.util.List;

import com.riccardo.giangiulio.dao.ActivityDAO;
import com.riccardo.giangiulio.models.Activity;

public class ActivityService {
    private final ActivityDAO activityDAO = new ActivityDAO();

    public Activity createActivity(Activity activity) {
        return activityDAO.createActivity(activity);
    }

    public Activity getActivityById(long activityId) {
        return activityDAO.getActivityById(activityId);
    }
       
    public List<Activity> getActivitiesByPlantationId(long plantationId) {
        return activityDAO.getActivitiesByPlantationId(plantationId);
    }

    public List<Activity> getPendingActivities(long plantationId) {
        return activityDAO.getPendingActivities(plantationId);
    }

    public void updateActivity(Activity activity) {
        activityDAO.updateActivity(activity);
    }

    public void deleteActivity(long activityId) {
        activityDAO.deleteActivity(activityId);
    }
} 