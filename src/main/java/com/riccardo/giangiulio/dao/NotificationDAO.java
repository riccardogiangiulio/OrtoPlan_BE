package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.riccardo.giangiulio.models.Notification;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class NotificationDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Notification createNotification(Notification notification) {
        String sql = "INSERT INTO public.\"Notification\" (message, sent_dt, opened, activity_id, user_id) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING notification_id";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, notification.getMessage());
            ps.setTimestamp(2, Timestamp.valueOf(notification.getSent_dt()));
            ps.setBoolean(3, notification.isOpened());
            ps.setLong(4, notification.getActivity().getActivityId());
            ps.setLong(5, notification.getUser().getUserId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                notification.setNotificationId(rs.getLong("notification_id"));
                return notification;
            }
            throw new RuntimeException("Failed to retrieve generated ID");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating notification: " + e.getMessage());
        }
    }

    public List<Notification> getUnreadNotifications(long userId) {
        String getUnreadNotificationsSQL = "SELECT * FROM public.\"Notification\" WHERE user_id = ? AND opened = false ORDER BY sent_dt DESC";
        List<Notification> notifications = new ArrayList<>();

        try (PreparedStatement psGetUnreadNotifications = connection.prepareStatement(getUnreadNotificationsSQL)) {
            psGetUnreadNotifications.setLong(1, userId);
            ResultSet rs = psGetUnreadNotifications.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification();
                notification.setNotificationId(rs.getLong("notification_id"));
                notification.setMessage(rs.getString("message"));
                notification.setOpened(rs.getBoolean("opened"));
                notification.setSent_dt(rs.getTimestamp("sent_dt").toLocalDateTime());
                notifications.add(notification);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving notifications", e);
        }
        return notifications;
    }    

    public void markAsRead(long notificationId) {
        String markAsReadSQL = "UPDATE public.\"Notification\" SET opened = true WHERE notification_id = ?";

        try (PreparedStatement psMarkAsRead = connection.prepareStatement(markAsReadSQL)) {
            psMarkAsRead.setLong(1, notificationId);
            psMarkAsRead.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating notification", e);
        }
    }

    public void deleteNotification(long notificationId) {
        String deleteNotificationSQL = "DELETE FROM public.\"Notification\" WHERE notification_id = ?";

        try (PreparedStatement psDeleteNotification = connection.prepareStatement(deleteNotificationSQL)) {
            psDeleteNotification.setLong(1, notificationId);
            psDeleteNotification.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting notification", e);
        }
    }
} 