package com.riccardo.giangiulio.services;

import java.util.List;

import com.riccardo.giangiulio.dao.ActivityTypeDAO;
import com.riccardo.giangiulio.models.ActivityType;

public class ActivityTypeService {
    private final ActivityTypeDAO activityTypeDAO = new ActivityTypeDAO();

    public ActivityType createActivityType(ActivityType activityType) {
        return activityTypeDAO.createActivityType(activityType);
    }
    
    public ActivityType getActivityTypeById(long activityTypeId) {
        return activityTypeDAO.getActivityTypeById(activityTypeId);
    }

    public List<ActivityType> getAllActivityTypes() {
        return activityTypeDAO.getAllActivityTypes();
    }

    public void deleteActivityType(long activityTypeId) {
        activityTypeDAO.deleteActivityType(activityTypeId);
    }
} 