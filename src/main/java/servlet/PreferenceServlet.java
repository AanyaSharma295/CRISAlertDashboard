package servlet;

import dao.AlertDAO;
import dao.PreferenceDAO;
import model.Alert;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/preferences")
public class PreferenceServlet extends HttpServlet {

    // When user lands on preferences page → show all alerts as checkboxes
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            // Not logged in → go back to login
            response.sendRedirect("login");
            return;
        }

        // Get all available alerts
        List<Alert> allAlerts = AlertDAO.getAllAlerts();

        // Get alerts this user already selected
        List<Integer> selectedAlertIds = PreferenceDAO.getUserPreferences(user.getUserId());

        request.setAttribute("allAlerts", allAlerts);
        request.setAttribute("selectedAlertIds", selectedAlertIds);
        request.getRequestDispatcher("/preferences.jsp").forward(request, response);
    }

    // When user submits their selection → save to DB
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // Get selected alert IDs from form
        String[] selectedAlerts = request.getParameterValues("alertIds");

        // Save to DB
        PreferenceDAO.saveUserPreferences(user.getUserId(), selectedAlerts);

        // Go to dashboard
        response.sendRedirect("alerts");
    }
}