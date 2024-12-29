package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.riccardo.giangiulio.models.Notification;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class NotificationDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public void createNotification(Notification notification) {
        String insertSQL = "INSERT INTO public.\"Notification\"(message, opened, sent_dt, activity_id, user_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setString(1, notification.getMessage());
            ps.setBoolean(2, notification.isOpened());
            ps.setObject(3, notification.getSent_dt());
            ps.setLong(4, notification.getActivity().getActivityId());
            ps.setLong(5, notification.getUser().getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la creazione della notifica", e);
        }
    }

    public List<Notification> getUnreadNotifications(long userId) {
        String selectSQL = "SELECT * FROM public.\"Notification\" WHERE user_id = ? AND opened = false ORDER BY sent_dt DESC";
        List<Notification> notifications = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification();
                notification.setNotificationId(rs.getLong("notification_id"));
                notification.setMessage(rs.getString("message"));
                notification.setOpened(rs.getBoolean("opened"));
                notification.setSent_dt(rs.getObject("sent_dt", LocalDateTime.class));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero delle notifiche", e);
        }

        return notifications;
    }

    public void markAsRead(long notificationId) {
        String updateSQL = "UPDATE public.\"Notification\" SET opened = true WHERE notification_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(updateSQL)) {
            ps.setLong(1, notificationId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Notifica non trovata");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento della notifica", e);
        }
    }

    public void deleteOldNotifications(long userId, LocalDateTime before) {
        String deleteSQL = "DELETE FROM public.\"Notification\" WHERE user_id = ? AND sent_dt < ?";

        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setLong(1, userId);
            ps.setObject(2, before);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la cancellazione delle notifiche", e);
        }
    }
} 