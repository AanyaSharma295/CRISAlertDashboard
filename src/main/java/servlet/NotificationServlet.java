package servlet;

import dao.AlertDAO;
import dao.NotificationDAO;
import model.Alert;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/notifications")
public class NotificationServlet extends HttpServlet {

    // Show notification preferences page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // Get user's selected alerts
        List<Alert> allAlerts = AlertDAO.getAllAlerts();
        Map<Integer, Map<String, String>> notifPrefs = 
            NotificationDAO.getUserNotificationPrefs(user.getUserId());

        request.setAttribute("allAlerts", allAlerts);
        request.setAttribute("notifPrefs", notifPrefs);
        request.getRequestDispatcher("/notifications.jsp").forward(request, response);
    }

    // Save notification preferences
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        List<Alert> allAlerts = AlertDAO.getAllAlerts();

        for (Alert alert : allAlerts) {
            String alertId = String.valueOf(alert.getAlertId());
            String notifyEmail = request.getParameter("email_" + alertId) != null ? "Y" : "N";
            String notifySms = request.getParameter("sms_" + alertId) != null ? "Y" : "N";
            String phone = request.getParameter("phone");

            NotificationDAO.saveNotificationPref(
                user.getUserId(), 
                alert.getAlertId(), 
                notifyEmail, 
                notifySms, 
                phone
            );
        }

        response.sendRedirect("alerts");
    }
}