package dao;

import model.Alert;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class AlertDAO {

    private static String DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    // This block runs once when AlertDAO is first used
    // It reads db.properties and loads connection details
    static {
        try {
            Properties props = new Properties();
            InputStream input = AlertDAO.class
                .getClassLoader()
                .getResourceAsStream("db.properties");
            props.load(input);

            DRIVER   = props.getProperty("db.driver");
            URL      = props.getProperty("db.url");
            USERNAME = props.getProperty("db.username");
            PASSWORD = props.getProperty("db.password");

            Class.forName(DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method connects to DB and returns a Connection object
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // This method fetches all active alerts WITH their live counts
    public static List<Alert> getAllAlerts() {
        List<Alert> alerts = new ArrayList<>();

        String sql = "SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = 'Y'";

        try (
            Connection con = getConnection();
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

                // Now run the count query for this alert
                int count = getCount(con, alert.getCountQuery());
                alert.setCount(count);

                alerts.add(alert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alerts;
    }

    // Runs the count query and returns the number
    private static int getCount(Connection con, String countQuery) {
        try (
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(countQuery);
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // Count query may fail if table doesn't exist yet
            // Return 0 safely
            return 0;
        }
        return 0;
    }
}