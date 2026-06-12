<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="model.Alert" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Notification Preferences</title>
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

        .header h1 { font-size: 20px; }
        .header span { font-size: 14px; }

        .phone-section {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
        }

        .phone-section label {
            font-size: 14px;
            color: #333;
            margin-right: 10px;
        }

        .phone-section input {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            width: 250px;
        }

        .table-container {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            overflow: hidden;
            margin-bottom: 30px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th {
            background-color: #1a3c5e;
            color: white;
            padding: 12px 15px;
            text-align: left;
            font-size: 14px;
        }

        td {
            padding: 12px 15px;
            border-bottom: 1px solid #eee;
            font-size: 13px;
            color: #333;
        }

        tr:hover { background-color: #f9f9f9; }

        .card-badge {
            background-color: #e8f0fe;
            color: #1a3c5e;
            padding: 3px 8px;
            border-radius: 10px;
            font-size: 11px;
        }

        input[type="checkbox"] {
            width: 16px;
            height: 16px;
            cursor: pointer;
        }

        .btn-container { text-align: center; }

        .btn {
            padding: 12px 40px;
            background-color: #1a3c5e;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
        }

        .btn:hover { background-color: #2a5c8e; }

        .nav-links {
            margin-bottom: 20px;
            display: flex;
            gap: 10px;
        }

        .nav-link {
            padding: 8px 16px;
            background-color: #1a3c5e;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 13px;
        }
    </style>
</head>
<body>

    <%
        User loggedUser = (User) session.getAttribute("loggedUser");
        List<Alert> allAlerts = (List<Alert>) request.getAttribute("allAlerts");
        Map<Integer, Map<String, String>> notifPrefs = 
            (Map<Integer, Map<String, String>>) request.getAttribute("notifPrefs");
        
        String savedPhone = "";
        if (!notifPrefs.isEmpty()) {
            Map<String, String> firstPref = notifPrefs.values().iterator().next();
            savedPhone = firstPref.get("phone") != null ? firstPref.get("phone") : "";
        }
    %>

    <div class="header">
        <h1>Notification Preferences</h1>
        <span>Welcome, <%= loggedUser.getFullName() %></span>
    </div>

    <div class="nav-links">
        <a href="alerts" class="nav-link">← Back to Dashboard</a>
        <a href="preferences" class="nav-link">My Alert Preferences</a>
    </div>

    <form action="notifications" method="post">

        <div class="phone-section">
            <label>Your Phone Number for SMS:</label>
            <input type="text" name="phone" 
                   value="<%= savedPhone %>" 
                   placeholder="Enter 10 digit mobile number" />
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>Alert Name</th>
                        <th>Card</th>
                        <th>Email Notification</th>
                        <th>SMS Notification</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Alert alert : allAlerts) {
                        Map<String, String> pref = notifPrefs.get(alert.getAlertId());
                        boolean emailChecked = pref != null && "Y".equals(pref.get("notifyEmail"));
                        boolean smsChecked = pref != null && "Y".equals(pref.get("notifySms"));
                    %>
                        <tr>
                            <td><%= alert.getAlertTitle() %></td>
                            <td><span class="card-badge"><%= alert.getCardName() %></span></td>
                            <td>
                                <input type="checkbox" 
                                       name="email_<%= alert.getAlertId() %>"
                                       <%= emailChecked ? "checked" : "" %> />
                            </td>
                            <td>
                                <input type="checkbox" 
                                       name="sms_<%= alert.getAlertId() %>"
                                       <%= smsChecked ? "checked" : "" %> />
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <div class="btn-container">
            <button type="submit" class="btn">Save Notification Preferences</button>
        </div>

    </form>

</body>
</html>