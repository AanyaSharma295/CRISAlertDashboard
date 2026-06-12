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

@WebServlet("/alerts")
public class AlertServlet extends HttpServlet {

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

        // Get ALL alerts from DB
        List<Alert> allAlerts = AlertDAO.getAllAlerts();

        // Get this user's selected alert IDs
        List<Integer> selectedAlertIds = PreferenceDAO.getUserPreferences(user.getUserId());

        // Filter — only keep alerts user selected
        List<Alert> userAlerts = new ArrayList<>();
        for (Alert alert : allAlerts) {
            if (selectedAlertIds.contains(alert.getAlertId())) {
                userAlerts.add(alert);
            }
        }

        // Group by card name
        Map<String, List<Alert>> alertsByCard = new LinkedHashMap<>();
        for (Alert alert : userAlerts) {
            String cardName = alert.getCardName();
            if (!alertsByCard.containsKey(cardName)) {
                alertsByCard.put(cardName, new ArrayList<>());
            }
            alertsByCard.get(cardName).add(alert);
        }

        // Send to index.jsp
        request.setAttribute("alertsByCard", alertsByCard);
        request.setAttribute("loggedUser", user);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}