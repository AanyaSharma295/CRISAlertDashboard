<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="model.Alert" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Panel</title>
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

        .nav-links {
            display: flex;
            gap: 15px;
            margin-bottom: 20px;
        }

        .nav-link {
            padding: 8px 16px;
            background-color: #1a3c5e;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 13px;
        }

        /* Add Alert Form */
        .add-form {
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            margin-bottom: 30px;
        }

        .add-form h2 {
            color: #1a3c5e;
            margin-bottom: 20px;
            font-size: 16px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
            margin-bottom: 15px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 6px;
        }

        .form-group label {
            font-size: 13px;
            color: #555;
            font-weight: bold;
        }

        .form-group input,
        .form-group select,
        .form-group textarea {
            padding: 9px 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 13px;
        }

        .form-group textarea {
            height: 80px;
            resize: vertical;
        }

        .form-full {
            grid-column: span 2;
        }

        .btn {
            padding: 10px 25px;
            background-color: #1a3c5e;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
        }

        .btn:hover { background-color: #2a5c8e; }

        /* Alerts Table */
        .table-container {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            overflow: hidden;
        }

        .table-container h2 {
            padding: 15px 20px;
            color: #1a3c5e;
            font-size: 16px;
            border-bottom: 1px solid #eee;
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
            font-size: 13px;
        }

        td {
            padding: 10px 15px;
            border-bottom: 1px solid #eee;
            font-size: 13px;
            color: #333;
        }

        tr:hover { background-color: #f9f9f9; }

        .badge-active {
            background-color: #27ae60;
            color: white;
            padding: 3px 10px;
            border-radius: 10px;
            font-size: 11px;
        }

        .badge-inactive {
            background-color: #e74c3c;
            color: white;
            padding: 3px 10px;
            border-radius: 10px;
            font-size: 11px;
        }

        .btn-toggle-on {
            padding: 5px 12px;
            background-color: #e74c3c;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 12px;
            cursor: pointer;
        }

        .btn-toggle-off {
            padding: 5px 12px;
            background-color: #27ae60;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 12px;
            cursor: pointer;
        }

        .query-preview {
            max-width: 200px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            color: #888;
            font-size: 11px;
        }
    </style>
</head>
<body>

    <%
        User loggedUser = (User) session.getAttribute("loggedUser");
        List<Alert> allAlerts = (List<Alert>) request.getAttribute("allAlerts");
    %>

    <div class="header">
        <h1>Admin Panel — Alert Management</h1>
        <span>Welcome, <%= loggedUser.getFullName() %></span>
    </div>

    <div class="nav-links">
        <a href="alerts" class="nav-link">← Back to Dashboard</a>
    </div>

    <!-- Add New Alert Form -->
    <div class="add-form">
        <h2>➕ Add New Alert</h2>
        <form action="admin" method="post">
            <input type="hidden" name="action" value="add" />
            <div class="form-grid">
                <div class="form-group">
                    <label>Alert Title</label>
                    <input type="text" name="alertTitle" 
                           placeholder="e.g. OHE Tripping in last 30 days" required />
                </div>
                <div class="form-group">
                    <label>Card Name</label>
                    <select name="cardName" required>
                        <option value="">-- Select Card --</option>
                        <option value="SOS & SMS">SOS & SMS</option>
                        <option value="Information Alerts">Information Alerts</option>
                        <option value="Working Alerts">Working Alerts</option>
                        <option value="OHE Use Cases">OHE Use Cases</option>
                        <option value="Maintenance Information">Maintenance Information</option>
                        <option value="Maintenance">Maintenance</option>
                        <option value="TDMS System">TDMS System</option>
                        <option value="Foot Patrol">Foot Patrol</option>
                        <option value="PSI Use Cases">PSI Use Cases</option>
                    </select>
                </div>
                <div class="form-group form-full">
                    <label>Count Query</label>
                    <textarea name="countQuery" 
                              placeholder="SELECT COUNT(*) FROM your_table WHERE condition"></textarea>
                </div>
                <div class="form-group form-full">
                    <label>Detail Query (for popup)</label>
                    <textarea name="detailQuery" 
                              placeholder="SELECT * FROM your_table WHERE condition"></textarea>
                </div>
            </div>
            <button type="submit" class="btn">Add Alert</button>
        </form>
    </div>

    <!-- All Alerts Table -->
    <div class="table-container">
        <h2>All Alerts</h2>
        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Alert Title</th>
                    <th>Card</th>
                    <th>Count Query</th>
                    <th>Created By</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <% for (Alert alert : allAlerts) { %>
                    <tr>
                        <td><%= alert.getAlertId() %></td>
                        <td><%= alert.getAlertTitle() %></td>
                        <td><%= alert.getCardName() %></td>
                        <td>
                            <div class="query-preview" title="<%= alert.getCountQuery() %>">
                                <%= alert.getCountQuery() %>
                            </div>
                        </td>
                        <td><%= alert.getCreatedBy() %></td>
                        <td>
                            <% if ("Y".equals(alert.getIsActive())) { %>
                                <span class="badge-active">Active</span>
                            <% } else { %>
                                <span class="badge-inactive">Inactive</span>
                            <% } %>
                        </td>
                        <td>
                            <form action="admin" method="post" style="display:inline">
                                <input type="hidden" name="action" value="toggle" />
                                <input type="hidden" name="alertId" value="<%= alert.getAlertId() %>" />
                                <input type="hidden" name="currentStatus" value="<%= alert.getIsActive() %>" />
                                <% if ("Y".equals(alert.getIsActive())) { %>
                                    <button type="submit" class="btn-toggle-on">Deactivate</button>
                                <% } else { %>
                                    <button type="submit" class="btn-toggle-off">Activate</button>
                                <% } %>
                            </form>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

</body>
</html>