package servlet;

import dao.AdminDAO;
import model.Alert;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        // Only admin can access this page
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect("login");
            return;
        }

        List<Alert> allAlerts = AdminDAO.getAllAlerts();
        request.setAttribute("allAlerts", allAlerts);
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            // Add new alert
            String title = request.getParameter("alertTitle");
            String cardName = request.getParameter("cardName");
            String countQuery = request.getParameter("countQuery");
            String detailQuery = request.getParameter("detailQuery");

            AdminDAO.addAlert(title, cardName, countQuery, detailQuery, user.getUsername());

        } else if ("toggle".equals(action)) {
            // Activate or deactivate alert
            int alertId = Integer.parseInt(request.getParameter("alertId"));
            String currentStatus = request.getParameter("currentStatus");
            String newStatus = "Y".equals(currentStatus) ? "N" : "Y";
            AdminDAO.toggleAlert(alertId, newStatus);
        }

        response.sendRedirect("admin");
    }
}