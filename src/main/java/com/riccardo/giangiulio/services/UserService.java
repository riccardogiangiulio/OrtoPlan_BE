package com.riccardo.giangiulio.services;

import com.riccardo.giangiulio.dao.UserDAO;
import com.riccardo.giangiulio.models.User;

public class UserService {
    private final UserDAO userDAO = new UserDAO(); 

    public void registerUser(User user) {
        userDAO.registerUser(user);
    }

    public User getUserById(long userId) {
        return userDAO.getUserById(userId);
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public void updateUser(User updates, long userId) {
        userDAO.updateUser(updates, userId);
    }

    public void updatePassword(long userId, String newPassword) {
        userDAO.updatePassword(userId, newPassword);
    }

    public void deleteUser(long userId) {
        userDAO.deleteUser(userId);
    }
} 