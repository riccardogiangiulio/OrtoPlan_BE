package com.riccardo.giangiulio.controller;

import com.riccardo.giangiulio.models.Notification;
import com.riccardo.giangiulio.services.NotificationService;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void registerRoutes(Javalin app) {
        app.post("/notifications", this::createNotification);
        app.get("/notifications/unread/{userId}", this::getUnreadNotifications);
        app.put("/notifications/{notificationId}/read", this::markAsRead);
        app.delete("/notifications/{notificationId}", this::deleteNotification);
    }

    private void createNotification(Context ctx) {
        try {
            Notification notification = ctx.bodyAsClass(Notification.class);
            Notification createdNotification = notificationService.createNotification(notification);
            ctx.status(201).json(createdNotification);
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    private void getUnreadNotifications(Context ctx) {
        try {
            long userId = Long.parseLong(ctx.pathParam("userId"));
            ctx.json(notificationService.getUnreadNotifications(userId));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID");
        } catch (RuntimeException e) {
            ctx.status(500).result(e.getMessage());
        }
    }

    private void markAsRead(Context ctx) {
        try {
            long notificationId = Long.parseLong(ctx.pathParam("notificationId"));
            notificationService.markAsRead(notificationId);
            ctx.status(200).result("Notification marked as read");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid notification ID");
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    private void deleteNotification(Context ctx) {
        try {
            long notificationId = Long.parseLong(ctx.pathParam("notificationId"));
            notificationService.deleteNotification(notificationId);
            ctx.status(204).result("Notification deleted successfully");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid notification ID");
        } catch (RuntimeException e) {
            ctx.status(400).result(e.getMessage());
        }
    }
}
