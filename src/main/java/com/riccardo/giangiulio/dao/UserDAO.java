package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.riccardo.giangiulio.models.User;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class UserDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    private boolean isEmailExists(String email) {
        String checkEmailSQL = "SELECT COUNT(*) FROM public.\"User\" WHERE email = ?";
        
        try (PreparedStatement psCheckEmail = connection.prepareStatement(checkEmailSQL)) {
            psCheckEmail.setString(1, email);
            ResultSet rs = psCheckEmail.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la verifica dell'email", e);
        }
        return false;
    }

    public void registerUser(User user) {
        if (isEmailExists(user.getEmail())) {
            throw new RuntimeException("Email già registrata");
        }

        String insertUserSQL = "INSERT INTO public.\"User\"(first_name, last_name, email, password) VALUES (?, ?, ?, ?)";

        try (PreparedStatement psInsertUser = connection.prepareStatement(insertUserSQL)) {
            psInsertUser.setString(1, user.getFirstName());
            psInsertUser.setString(2, user.getLastName());
            psInsertUser.setString(3, user.getEmail());
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            psInsertUser.setString(4, hashedPassword);
            psInsertUser.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la creazione dell'utente", e);
        }
    }

    public User getUserById(long userId) {
        String getUserByIdSQL = "SELECT * FROM public.\"User\" WHERE user_id = ?";

        try (PreparedStatement psSelect = connection.prepareStatement(getUserByIdSQL)) {
            psSelect.setLong(1, userId);
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getLong("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero dell'utente", e);
        }
        return null;
    }

    public void updateUser(User user) {
        String updateUserSQL = "UPDATE public.\"User\" SET first_name = ?, last_name = ?, email = ? WHERE user_id = ?";

        try (PreparedStatement psUpdateUser = connection.prepareStatement(updateUserSQL)) {
            psUpdateUser.setString(1, user.getFirstName());
            psUpdateUser.setString(2, user.getLastName());
            psUpdateUser.setString(3, user.getEmail());

            psUpdateUser.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento dell'utente", e);
        }
    }

    public void updatePassword(long userId, String newPassword) {
        String updatePasswordSQL = "UPDATE public.\"User\" SET password = ? WHERE user_id = ?";

        try (PreparedStatement psUpdatePassword = connection.prepareStatement(updatePasswordSQL)) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            psUpdatePassword.setString(1, hashedPassword);
            psUpdatePassword.setLong(2, userId);

            psUpdatePassword.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento della password", e);
        }
    }

    public void deleteUser(long userId) {
        String deleteUserSQL = "DELETE FROM public.\"User\" WHERE user_id = ?";

        try (PreparedStatement psDeleteUser = connection.prepareStatement(deleteUserSQL)) {
            psDeleteUser.setLong(1, userId);
            psDeleteUser.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la cancellazione dell'utente", e);
        }
    }

    public User login(String email, String password) {
        String loginSQL = "SELECT * FROM public.\"User\" WHERE email = ?";

        try (PreparedStatement psLogin = connection.prepareStatement(loginSQL)) {
            psLogin.setString(1, email);
            ResultSet rs = psLogin.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Email non registrata");
            }

            String hashedPassword = rs.getString("password");
            if (!BCrypt.checkpw(password, hashedPassword)) {
                throw new RuntimeException("Email o password non corrette");
            }

            return new User(
                rs.getLong("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                null
            );

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il login", e);
        }
    }
}

