package dao;

import java.sql.*;
import java.util.*;

public class PreferenceDAO {

    // Get list of alert IDs this user has selected
    public static List<Integer> getUserPreferences(int userId) {
        List<Integer> alertIds = new ArrayList<>();
        String sql = "SELECT ALERT_ID FROM USER_ALERT_PREFERENCES WHERE USER_ID = ?";

        try (
            Connection con = AlertDAO.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                alertIds.add(rs.getInt("ALERT_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alertIds;
    }

    // Save user's selected alerts to DB
    public static void saveUserPreferences(int userId, String[] alertIds) {
        // First delete old preferences
        String deleteSql = "DELETE FROM USER_ALERT_PREFERENCES WHERE USER_ID = ?";

        // Then insert new ones
        String insertSql = "INSERT INTO USER_ALERT_PREFERENCES (USER_ID, ALERT_ID) VALUES (?, ?)";

        try (
            Connection con = AlertDAO.getConnection();
            PreparedStatement deletePs = con.prepareStatement(deleteSql);
            PreparedStatement insertPs = con.prepareStatement(insertSql);
        ) {
            // Delete old
            deletePs.setInt(1, userId);
            deletePs.executeUpdate();

            // Insert new
            if (alertIds != null) {
                for (String alertId : alertIds) {
                    insertPs.setInt(1, userId);
                    insertPs.setInt(2, Integer.parseInt(alertId));
                    insertPs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}