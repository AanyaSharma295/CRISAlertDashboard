package service;

import dao.AlertDAO;
import dao.NotificationDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AlertScheduler implements Runnable {

    @Override
    public void run() {
        System.out.println("Scheduler running at: " + new Date());

        try {
            // Get all notification preferences
            List<Map<String, String>> prefs = NotificationDAO.getAllNotificationPrefs();

            for (Map<String, String> pref : prefs) {
                String countQuery = pref.get("countQuery");
                String alertTitle = pref.get("alertTitle");
                String email = pref.get("email");
                String notifyEmail = pref.get("notifyEmail");

                // Run the count query
                int count = getCount(countQuery);

                // Only notify if count > 0
                if (count > 0) {
                    if ("Y".equals(notifyEmail) && email != null && !email.isEmpty()) {
                        String subject = "TDMS Alert: " + alertTitle;
                        String body = "Dear Officer,\n\n" +
                                      "This is an automated alert from TDMS Dashboard.\n\n" +
                                      "Alert: " + alertTitle + "\n" +
                                      "Current Count: " + count + "\n\n" +
                                      "Please login to the dashboard for more details.\n\n" +
                                      "Regards,\nTDMS System";
                        EmailService.sendEmail(email, subject, body);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getCount(String countQuery) {
        try (
            Connection con = AlertDAO.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(countQuery);
        ) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}