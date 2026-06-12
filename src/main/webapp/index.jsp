<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="model.Alert" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CRIS Alert Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        /* Popup Styles */
        .modal-overlay {
            display: none;
            position: fixed;
            top: 0; left: 0;
            width: 100%; height: 100%;
            background: rgba(0,0,0,0.5);
            z-index: 1000;
            justify-content: center;
            align-items: center;
        }

        .modal-overlay.active {
            display: flex;
        }

        .modal {
            background: white;
            border-radius: 10px;
            width: 80%;
            max-height: 80vh;
            overflow-y: auto;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        }

        .modal-header {
            background-color: #1a3c5e;
            color: white;
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            position: sticky;
            top: 0;
        }

        .modal-header h3 { font-size: 16px; }

        .close-btn {
            background: none;
            border: none;
            color: white;
            font-size: 22px;
            cursor: pointer;
        }

        .modal-body { padding: 20px; }

        .modal-table {
            width: 100%;
            border-collapse: collapse;
            font-size: 13px;
        }

        .modal-table th {
            background-color: #1a3c5e;
            color: white;
            padding: 10px;
            text-align: left;
        }

        .modal-table td {
            padding: 8px 10px;
            border-bottom: 1px solid #eee;
        }

        .modal-table tr:hover { background-color: #f5f5f5; }

        .loading {
            text-align: center;
            padding: 30px;
            color: #888;
        }

        .no-data {
            text-align: center;
            padding: 30px;
            color: #888;
        }

        .nav-bar {
            display: flex;
            gap: 15px;
            align-items: center;
        }

        .nav-bar a {
            color: white;
            text-decoration: none;
            font-size: 13px;
        }

        .nav-bar a:hover { text-decoration: underline; }
    </style>
</head>
<body>

    <div class="header">
        <h1>TDMS Alert Dashboard</h1>
        <div class="nav-bar">
    <%
        User loggedUser = (User) request.getAttribute("loggedUser");
        if (loggedUser != null) {
    %>
        <span>Welcome, <%= loggedUser.getFullName() %></span>
        <a href="preferences">My Alerts</a>
        <a href="notifications">Notifications</a>
        <% if ("ADMIN".equals(loggedUser.getRole())) { %>
            <a href="admin">Admin Panel</a>
        <% } %>
        <a href="logout">Logout</a>
    <% } %>
</div>
    </div>

    <div class="dashboard">
        <%
            Map<String, List<Alert>> alertsByCard =
                (Map<String, List<Alert>>) request.getAttribute("alertsByCard");

            if (alertsByCard != null && !alertsByCard.isEmpty()) {
                for (Map.Entry<String, List<Alert>> entry : alertsByCard.entrySet()) {
                    String cardName = entry.getKey();
                    List<Alert> alerts = entry.getValue();
        %>
                <div class="card">
                    <div class="card-header"><%= cardName %></div>
                    <div class="card-body">
                        <% for (Alert alert : alerts) { %>
                            <div class="alert-item" 
                                 onclick="openPopup(<%= alert.getAlertId() %>, '<%= alert.getAlertTitle() %>')">
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
            <div style="padding:20px; color:#888;">
                No alerts selected. 
                <a href="preferences">Click here to select alerts.</a>
            </div>
        <% } %>
    </div>

    <!-- Popup Modal -->
    <div class="modal-overlay" id="modalOverlay">
        <div class="modal">
            <div class="modal-header">
                <h3 id="modalTitle">Alert Details</h3>
                <button class="close-btn" onclick="closePopup()">✕</button>
            </div>
            <div class="modal-body" id="modalBody">
                <div class="loading">Loading...</div>
            </div>
        </div>
    </div>

    <script>
        function openPopup(alertId, alertTitle) {
            // Show modal
            document.getElementById('modalTitle').innerText = alertTitle;
            document.getElementById('modalBody').innerHTML = '<div class="loading">Loading...</div>';
            document.getElementById('modalOverlay').classList.add('active');

            // Fetch data from server
            fetch('popup?alertId=' + alertId)
                .then(response => response.json())
                .then(data => {
                    if (data.length === 0) {
                        document.getElementById('modalBody').innerHTML = 
                            '<div class="no-data">No data found for this alert.</div>';
                        return;
                    }

                    // Build table from JSON data
                    let html = '<table class="modal-table"><thead><tr>';
                    
                    // Headers
                    Object.keys(data[0]).forEach(key => {
                        html += '<th>' + key + '</th>';
                    });
                    html += '</tr></thead><tbody>';

                    // Rows
                    data.forEach(row => {
                        html += '<tr>';
                        Object.values(row).forEach(val => {
                            html += '<td>' + val + '</td>';
                        });
                        html += '</tr>';
                    });

                    html += '</tbody></table>';
                    document.getElementById('modalBody').innerHTML = html;
                })
                .catch(err => {
                    document.getElementById('modalBody').innerHTML = 
                        '<div class="no-data">Error loading data.</div>';
                });
        }

        function closePopup() {
            document.getElementById('modalOverlay').classList.remove('active');
        }

        // Close popup if user clicks outside
        document.getElementById('modalOverlay').addEventListener('click', function(e) {
            if (e.target === this) closePopup();
        });
    </script>

</body>
</html>