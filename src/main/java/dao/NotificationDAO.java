package dao;

import java.sql.*;
import java.util.*;

public class NotificationDAO {

    // Get notification preferences for a user
    public static Map<Integer, Map<String, String>> getUserNotificationPrefs(int userId) {
        Map<Integer, Map<String, String>> prefs = new HashMap<>();
        String sql = "SELECT * FROM USER_NOTIFICATION_PREFERENCES WHERE USER_ID = ?";

        try (
            Connection con = AlertDAO.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> pref = new HashMap<>();
                pref.put("notifyEmail", rs.getString("NOTIFY_EMAIL"));
                pref.put("notifySms", rs.getString("NOTIFY_SMS"));
                pref.put("phone", rs.getString("PHONE_NUMBER"));
                prefs.put(rs.getInt("ALERT_ID"), pref);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prefs;
    }

    // Save or update notification preference
    public static void saveNotificationPref(int userId, int alertId, 
                                            String notifyEmail, String notifySms, 
                                            String phone) {
        String checkSql = "SELECT COUNT(*) FROM USER_NOTIFICATION_PREFERENCES " +
                          "WHERE USER_ID = ? AND ALERT_ID = ?";
        String updateSql = "UPDATE USER_NOTIFICATION_PREFERENCES " +
                           "SET NOTIFY_EMAIL=?, NOTIFY_SMS=?, PHONE_NUMBER=? " +
                           "WHERE USER_ID=? AND ALERT_ID=?";
        String insertSql = "INSERT INTO USER_NOTIFICATION_PREFERENCES " +
                           "(USER_ID, ALERT_ID, NOTIFY_EMAIL, NOTIFY_SMS, PHONE_NUMBER) " +
                           "VALUES (?,?,?,?,?)";

        try (Connection con = AlertDAO.getConnection()) {
            // Check if record exists
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, userId);
            checkPs.setInt(2, alertId);
            ResultSet rs = checkPs.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Update existing
                PreparedStatement updatePs = con.prepareStatement(updateSql);
                updatePs.setString(1, notifyEmail);
                updatePs.setString(2, notifySms);
                updatePs.setString(3, phone);
                updatePs.setInt(4, userId);
                updatePs.setInt(5, alertId);
                updatePs.executeUpdate();
            } else {
                // Insert new
                PreparedStatement insertPs = con.prepareStatement(insertSql);
                insertPs.setInt(1, userId);
                insertPs.setInt(2, alertId);
                insertPs.setString(3, notifyEmail);
                insertPs.setString(4, notifySms);
                insertPs.setString(5, phone);
                insertPs.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all users and their notification prefs for the scheduler
    public static List<Map<String, String>> getAllNotificationPrefs() {
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT u.EMAIL, u.PHONE_NUMBER, unp.NOTIFY_EMAIL, " +
                     "unp.NOTIFY_SMS, unp.ALERT_ID, am.ALERT_TITLE, am.COUNT_QUERY " +
                     "FROM USER_NOTIFICATION_PREFERENCES unp " +
                     "JOIN USERS u ON unp.USER_ID = u.USER_ID " +
                     "JOIN ALERT_MASTER am ON unp.ALERT_ID = am.ALERT_ID " +
                     "WHERE unp.NOTIFY_EMAIL = 'Y' OR unp.NOTIFY_SMS = 'Y'";

        try (
            Connection con = AlertDAO.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
        ) {
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("email", rs.getString("EMAIL"));
                row.put("phone", rs.getString("PHONE_NUMBER"));
                row.put("notifyEmail", rs.getString("NOTIFY_EMAIL"));
                row.put("notifySms", rs.getString("NOTIFY_SMS"));
                row.put("alertTitle", rs.getString("ALERT_TITLE"));
                row.put("countQuery", rs.getString("COUNT_QUERY"));
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}