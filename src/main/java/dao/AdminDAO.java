package dao;

import model.Alert;
import java.sql.*;
import java.util.*;

public class AdminDAO {

    public static List<Alert> getAllAlerts() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT * FROM ALERT_MASTER ORDER BY CARD_NAME, ALERT_ID";

        try (
            Connection con = AlertDAO.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
        ) {
            while (rs.next()) {
                Alert alert = new Alert();
                alert.setAlertId(rs.getInt("ALERT_ID"));
                alert.setAlertTitle(rs.getString("ALERT_TITLE"));
                alert.setCardName(rs.getString("CARD_NAME"));
                alert.setCountQuery(rs.getString("COUNT_QUERY"));
                alert.setDetailQuery(rs.getString("DETAIL_QUERY"));
                alert.setIsActive(rs.getString("IS_ACTIVE"));
                alert.setCreatedBy(rs.getString("CREATED_BY"));
                alerts.add(alert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alerts;
    }

    public static void addAlert(String title, String cardName, 
                                 String countQuery, String detailQuery, 
                                 String createdBy) {
        String sql = "INSERT INTO ALERT_MASTER (ALERT_TITLE, CARD_NAME, COUNT_QUERY, " +
                     "DETAIL_QUERY, IS_ACTIVE, CREATED_BY) VALUES (?, ?, ?, ?, 'Y', ?)";

        try (
            Connection con = AlertDAO.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, title);
            ps.setString(2, cardName);
            ps.setString(3, countQuery);
            ps.setString(4, detailQuery);
            ps.setString(5, createdBy);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void toggleAlert(int alertId, String newStatus) {
        String sql = "UPDATE ALERT_MASTER SET IS_ACTIVE = ? WHERE ALERT_ID = ?";

        try (
            Connection con = AlertDAO.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, newStatus);
            ps.setInt(2, alertId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}