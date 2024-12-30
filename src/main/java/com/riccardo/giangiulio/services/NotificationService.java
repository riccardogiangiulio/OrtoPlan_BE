package com.riccardo.giangiulio.services;

import java.util.List;

import com.riccardo.giangiulio.dao.NotificationDAO;
import com.riccardo.giangiulio.models.Notification;

public class NotificationService {
    private final NotificationDAO notificationDAO = new NotificationDAO();

    public Notification createNotification(Notification notification) {
        return notificationDAO.createNotification(notification);
    }

    public List<Notification> getUnreadNotifications(long userId) {
        return notificationDAO.getUnreadNotifications(userId);
    }

    public void markAsRead(long notificationId) {
        notificationDAO.markAsRead(notificationId);
    }

    public void deleteNotification(long notificationId) {
        notificationDAO.deleteNotification(notificationId);
    }
} 