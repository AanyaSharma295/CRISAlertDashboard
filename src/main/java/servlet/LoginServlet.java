package servlet;

import dao.UserDAO;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // When login page loads (GET request) → just show the login page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // When user submits the form (POST request) → check credentials
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Ask UserDAO to validate
        User user = UserDAO.validateUser(username, password);

        if (user != null) {
            // Login successful → save user in session
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", user);

            // Redirect to preferences page
            response.sendRedirect("preferences");
        } else {
            // Login failed → go back to login with error
            request.setAttribute("error", "Invalid username or password!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}