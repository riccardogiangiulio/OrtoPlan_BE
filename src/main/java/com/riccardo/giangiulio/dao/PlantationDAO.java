package com.riccardo.giangiulio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.riccardo.giangiulio.models.Plantation;
import com.riccardo.giangiulio.utility.database.DatabaseConnection;

public class PlantationDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public void createPlantation(Plantation plantation) {
        String insertSQL = "INSERT INTO public.\"Plantation\"(name, city, start_date, end_date, user_id, plant_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setString(1, plantation.getName());
            ps.setString(2, plantation.getCity());
            ps.setObject(3, plantation.getStartDate());
            ps.setObject(4, plantation.getEndDate());
            ps.setLong(5, plantation.getUser().getUserId());
            ps.setLong(6, plantation.getPlant().getPlantId());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la creazione della piantagione", e);
        }
    }

    public List<Plantation> getPlantationsByUserId(long userId) {
        String selectSQL = "SELECT * FROM public.\"Plantation\" WHERE user_id = ?";
        List<Plantation> plantations = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Plantation plantation = new Plantation();
                plantation.setPlantationId(rs.getLong("plantation_id"));
                plantation.setName(rs.getString("name"));
                plantation.setCity(rs.getString("city"));
                plantation.setStartDate(rs.getObject("start_date", LocalDate.class));
                plantation.setEndDate(rs.getObject("end_date", LocalDate.class));
                plantations.add(plantation);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero delle piantagioni", e);
        }

        return plantations;
    }

    public Plantation getPlantationById(long plantationId) {
        String selectSQL = "SELECT * FROM public.\"Plantation\" WHERE plantation_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setLong(1, plantationId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Piantagione non trovata");
            }

            Plantation plantation = new Plantation();
            plantation.setPlantationId(rs.getLong("plantation_id"));
            plantation.setName(rs.getString("name"));
            plantation.setCity(rs.getString("city"));
            plantation.setStartDate(rs.getObject("start_date", LocalDate.class));
            plantation.setEndDate(rs.getObject("end_date", LocalDate.class));
            return plantation;

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero della piantagione", e);
        }
    }

    public void updatePlantation(Plantation plantation) {
        String updateSQL = "UPDATE public.\"Plantation\" SET name = ?, city = ?, start_date = ?, end_date = ? WHERE plantation_id = ? AND user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(updateSQL)) {
            ps.setString(1, plantation.getName());
            ps.setString(2, plantation.getCity());
            ps.setObject(3, plantation.getStartDate());
            ps.setObject(4, plantation.getEndDate());
            ps.setLong(5, plantation.getPlantationId());
            ps.setLong(6, plantation.getUser().getUserId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Piantagione non trovata o non autorizzato");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento della piantagione", e);
        }
    }

    public void deletePlantation(long plantationId, long userId) {
        String deleteSQL = "DELETE FROM public.\"Plantation\" WHERE plantation_id = ? AND user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setLong(1, plantationId);
            ps.setLong(2, userId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Piantagione non trovata o non autorizzato");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la cancellazione della piantagione", e);
        }
    }

    // Recupera le piantagioni attive di un utente (utile per notifiche e monitoraggio)
    public List<Plantation> getActivePlantations(long userId) {
        String selectSQL = "SELECT * FROM public.\"Plantation\" WHERE user_id = ? AND end_date >= CURRENT_DATE";
        List<Plantation> plantations = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Plantation plantation = new Plantation();
                plantation.setPlantationId(rs.getLong("plantation_id"));
                plantation.setName(rs.getString("name"));
                plantation.setCity(rs.getString("city"));
                plantation.setStartDate(rs.getObject("start_date", LocalDate.class));
                plantation.setEndDate(rs.getObject("end_date", LocalDate.class));
                plantations.add(plantation);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero delle piantagioni attive", e);
        }

        return plantations;
    }

    // Recupera le piantagioni per città (utile per i dati meteo)
    public List<Plantation> getPlantationsByCity(String city, long userId) {
        String selectSQL = "SELECT * FROM public.\"Plantation\" WHERE city = ? AND user_id = ?";
        List<Plantation> plantations = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setString(1, city);
            ps.setLong(2, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Plantation plantation = new Plantation();
                plantation.setPlantationId(rs.getLong("plantation_id"));
                plantation.setName(rs.getString("name"));
                plantation.setCity(rs.getString("city"));
                plantation.setStartDate(rs.getObject("start_date", LocalDate.class));
                plantation.setEndDate(rs.getObject("end_date", LocalDate.class));
                plantations.add(plantation);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero delle piantagioni per città", e);
        }

        return plantations;
    }

    // Verifica se una piantagione è nel periodo di coltivazione
    public boolean isPlantationActive(long plantationId) {
        String selectSQL = "SELECT COUNT(*) FROM public.\"Plantation\" WHERE plantation_id = ? AND start_date <= CURRENT_DATE AND end_date >= CURRENT_DATE";

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setLong(1, plantationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la verifica dello stato della piantagione", e);
        }

        return false;
    }
} 