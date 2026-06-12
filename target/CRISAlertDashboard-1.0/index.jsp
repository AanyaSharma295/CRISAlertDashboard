<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="model.Alert" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CRIS Alert Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<%@ page import="model.User" %>
    <div class="header">
    <h1>TDMS Alert Dashboard</h1>
    <div>
        <%
            User loggedUser = (User) request.getAttribute("loggedUser");
            if (loggedUser != null) {
        %>
            <span style="color:white; margin-right:20px;">
                Welcome, <%= loggedUser.getFullName() %>
            </span>
            <a href="preferences" style="color:white; margin-right:15px;">
                My Alerts
            </a>
            <a href="notifications" style="color:white; margin-right:15px;">
                Notifications
            </a>
            <a href="logout" style="color:white;">Logout</a>
        <% } %>
    </div>
</div>

    <div class="dashboard">
        <%
            Map<String, List<Alert>> alertsByCard = 
                (Map<String, List<Alert>>) request.getAttribute("alertsByCard");

            if (alertsByCard != null) {
                for (Map.Entry<String, List<Alert>> entry : alertsByCard.entrySet()) {
                    String cardName = entry.getKey();
                    List<Alert> alerts = entry.getValue();
        %>
                <div class="card">
                    <div class="card-header">
                        <%= cardName %>
                    </div>
                    <div class="card-body">
                        <% for (Alert alert : alerts) { %>
                            <div class="alert-item">
                                <span class="alert-count"><%= alert.getCount() %></span>
                                <span class="alert-title"><%= alert.getAlertTitle() %></span>
                            </div>
                        <% } %>
                    </div>
                </div>
        <%
                }
            } else {
        %>
                <p>No alerts found.</p>
        <%
            }
        %>
    </div>

    <script src="js/dashboard.js"></script>
</body>
</html>