<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="model.Alert" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Select Your Alerts</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            background-color: #f0f2f5;
            padding: 30px;
        }

        .header {
            background-color: #1a3c5e;
            color: white;
            padding: 15px 30px;
            border-radius: 8px;
            margin-bottom: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .header h1 {
            font-size: 20px;
        }

        .header span {
            font-size: 14px;
        }

        .instruction {
            background: white;
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            color: #555;
            font-size: 14px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
        }

        .alerts-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 15px;
            margin-bottom: 30px;
        }

        .alert-option {
            background: white;
            border-radius: 8px;
            padding: 15px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            display: flex;
            align-items: center;
            gap: 10px;
            cursor: pointer;
            transition: background 0.2s;
        }

        .alert-option:hover {
            background-color: #f0f7ff;
        }

        .alert-option input[type="checkbox"] {
            width: 18px;
            height: 18px;
            cursor: pointer;
        }

        .alert-option label {
            font-size: 13px;
            color: #333;
            cursor: pointer;
        }

        .card-label {
            font-size: 11px;
            color: #888;
            margin-top: 3px;
        }

        .btn-container {
            text-align: center;
        }

        .btn {
            padding: 12px 40px;
            background-color: #1a3c5e;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
        }

        .btn:hover {
            background-color: #2a5c8e;
        }

        .select-all {
            margin-bottom: 15px;
            display: flex;
            gap: 10px;
        }

        .btn-small {
            padding: 6px 15px;
            background-color: #1a3c5e;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 13px;
            cursor: pointer;
        }
    </style>
</head>
<body>

    <%
        User loggedUser = (User) session.getAttribute("loggedUser");
        List<Alert> allAlerts = (List<Alert>) request.getAttribute("allAlerts");
        List<Integer> selectedAlertIds = (List<Integer>) request.getAttribute("selectedAlertIds");
    %>

    <div class="header">
        <h1>Select Your Alerts</h1>
        <span>Welcome, <%= loggedUser.getFullName() %></span>
    </div>

    <div class="instruction">
        Select the alerts you want to see on your dashboard. Only your selected alerts will be shown.
    </div>

    <form action="preferences" method="post">
        <div class="select-all">
            <button type="button" class="btn-small" onclick="selectAll()">Select All</button>
            <button type="button" class="btn-small" onclick="deselectAll()">Deselect All</button>
        </div>

        <div class="alerts-grid">
            <% for (Alert alert : allAlerts) { 
                boolean isSelected = selectedAlertIds.contains(alert.getAlertId());
            %>
                <div class="alert-option">
                    <input type="checkbox" 
                           name="alertIds" 
                           value="<%= alert.getAlertId() %>"
                           id="alert_<%= alert.getAlertId() %>"
                           <%= isSelected ? "checked" : "" %> />
                    <div>
                        <label for="alert_<%= alert.getAlertId() %>">
                            <%= alert.getAlertTitle() %>
                        </label>
                        <div class="card-label"><%= alert.getCardName() %></div>
                    </div>
                </div>
            <% } %>
        </div>

        <div class="btn-container">
            <button type="submit" class="btn">Save & View Dashboard</button>
        </div>
    </form>

    <script>
        function selectAll() {
            document.querySelectorAll('input[type="checkbox"]').forEach(cb => cb.checked = true);
        }
        function deselectAll() {
            document.querySelectorAll('input[type="checkbox"]').forEach(cb => cb.checked = false);
        }
    </script>

</body>
</html>