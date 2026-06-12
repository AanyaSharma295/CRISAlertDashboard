package servlet;

import dao.AlertDAO;
import model.Alert;
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

        // Go to DAO and get all alerts
        List<Alert> alerts = AlertDAO.getAllAlerts();

        // Group alerts by card name
        Map<String, List<Alert>> alertsByCard = new LinkedHashMap<>();
        for (Alert alert : alerts) {
            String cardName = alert.getCardName();
            if (!alertsByCard.containsKey(cardName)) {
                alertsByCard.put(cardName, new ArrayList<>());
            }
            alertsByCard.get(cardName).add(alert);
        }

        // Send the data to index.jsp
        request.setAttribute("alertsByCard", alertsByCard);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}