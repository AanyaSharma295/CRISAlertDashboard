package servlet;

import dao.AlertDAO;
import dao.PopupDAO;
import model.Alert;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import com.google.gson.Gson;

@WebServlet("/popup")
public class PopupServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String alertIdStr = request.getParameter("alertId");
        int alertId = Integer.parseInt(alertIdStr);

        // Get the alert details
        Alert alert = AlertDAO.getAlertById(alertId);

        if (alert == null) {
            response.setStatus(404);
            return;
        }

        // Run the detail query and get results
        List<Map<String, String>> results = PopupDAO.getDetailData(alert.getDetailQuery());

        // Send back as JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(results));
        out.flush();
    }
}