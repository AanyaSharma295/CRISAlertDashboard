# CRIS Alert Dashboard

A dynamic, full-stack web application built for the **Centre for Railway Information Systems (CRIS)** to manage and monitor railway operational alerts in real time. This system replaces the static, hardcoded alert dashboard of the TDMS (Traction Distribution Management System) with a fully database-driven, personalized, and notification-enabled platform.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Prerequisites](#prerequisites)
- [Installation and Setup](#installation-and-setup)
- [Running the Application](#running-the-application)
- [Usage Guide](#usage-guide)
- [Default Credentials](#default-credentials)
- [Architecture](#architecture)

---

## Project Overview

The existing TDMS alert dashboard required a developer to write a new JSP file and redeploy the entire application every time a new alert needed to be added. This project solves that problem by making the dashboard fully dynamic and database-driven.

The system is built across three levels of complexity:

| Level | Feature |
|---|---|
| L1 | Dynamic alert dashboard — all configuration stored in DB, zero redeployment needed |
| L2 | User authentication with personalized, customizable dashboards |
| L3 | Automated email notification system with user-defined preferences |

---

## Features

- **Dynamic Alert Management** — Add, edit, or deactivate alerts directly from the database or admin UI without touching any code
- **9 Alert Cards** — Alerts organized into SOS & SMS, Information Alerts, Working Alerts, OHE Use Cases, Maintenance Information, Maintenance, TDMS System, Foot Patrol, and PSI Use Cases
- **User Authentication** — Secure login with session management
- **Personalized Dashboards** — Each user selects which alerts they want to see
- **Frequency-Based Prioritization** — Alerts the user selects most often automatically float to the top of their preferences
- **Popup Detail Tables** — Click any alert to see a detailed data table in a popup, powered by dynamic queries stored in DB
- **Role-Based Access Control** — Admin users get access to the Admin Panel; regular users do not
- **Email Notifications** — Users configure email notifications per alert; a background scheduler sends automated emails every 30 minutes when alert counts are non-zero
- **Admin Panel** — UI for admins to add new alerts and activate/deactivate existing ones without opening the database

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | HTML, CSS, JavaScript, JSP |
| Backend | Java, Servlets |
| Database | MySQL 8.0 |
| Server | Apache Tomcat 10 |
| Email | JavaMail API (SMTP via Gmail) |
| Build Tool | Apache Maven |
| Version Control | Git |

---

## Project Structure

```
CRISAlertDashboard/
│
├── src/main/
│   ├── webapp/                        # Frontend files
│   │   ├── index.jsp                  # Main dashboard
│   │   ├── login.jsp                  # Login page
│   │   ├── preferences.jsp            # Alert selection page
│   │   ├── notifications.jsp          # Notification preferences
│   │   ├── admin.jsp                  # Admin panel
│   │   ├── WEB-INF/
│   │   │   └── web.xml                # App configuration
│   │   ├── css/
│   │   │   └── style.css              # Global styles
│   │   └── js/
│   │       └── dashboard.js           # Frontend scripts
│   │
│   └── java/
│       ├── model/                     # Data models
│       │   ├── Alert.java             # Alert entity
│       │   └── User.java              # User entity
│       │
│       ├── dao/                       # Database access layer
│       │   ├── AlertDAO.java          # Alert DB operations
│       │   ├── UserDAO.java           # User DB operations
│       │   ├── PreferenceDAO.java     # User alert preferences
│       │   ├── NotificationDAO.java   # Notification preferences
│       │   ├── PopupDAO.java          # Popup detail data
│       │   └── AdminDAO.java          # Admin operations
│       │
│       ├── servlet/                   # Request handlers
│       │   ├── AlertServlet.java      # Dashboard handler
│       │   ├── LoginServlet.java      # Login/logout handler
│       │   ├── LogoutServlet.java     # Session termination
│       │   ├── PreferenceServlet.java # Preferences handler
│       │   ├── NotificationServlet.java # Notification handler
│       │   ├── PopupServlet.java      # Popup data handler
│       │   └── AdminServlet.java      # Admin panel handler
│       │
│       └── service/                   # Background services
│           ├── EmailService.java      # Email sending logic
│           ├── AlertScheduler.java    # Scheduled alert checker
│           └── AppContextListener.java # Scheduler starter
│
├── src/main/resources/
│   └── db.properties                  # Database configuration
│
├── pom.xml                            # Maven dependencies
├── run.bat                            # One-click run script
└── README.md
```

---

## Database Schema

### Tables

**ALERT_MASTER** — Master list of all alerts
```sql
ALERT_ID        INT (PK, AUTO_INCREMENT)
ALERT_TITLE     VARCHAR(255)
CARD_NAME       VARCHAR(100)
COUNT_QUERY     TEXT
DETAIL_QUERY    TEXT
IS_ACTIVE       CHAR(1) DEFAULT 'Y'
CREATED_BY      VARCHAR(100)
CREATED_DATE    TIMESTAMP
```

**USERS** — System users
```sql
USER_ID         INT (PK, AUTO_INCREMENT)
USERNAME        VARCHAR(100) UNIQUE
PASSWORD        VARCHAR(100)
FULL_NAME       VARCHAR(200)
EMAIL           VARCHAR(200)
PHONE_NUMBER    VARCHAR(15)
ROLE            VARCHAR(50) DEFAULT 'USER'
CREATED_DATE    TIMESTAMP
```

**USER_ALERT_PREFERENCES** — Which alerts each user has selected
```sql
PREF_ID         INT (PK, AUTO_INCREMENT)
USER_ID         INT (FK → USERS)
ALERT_ID        INT (FK → ALERT_MASTER)
SELECTION_COUNT INT DEFAULT 0
```

**USER_NOTIFICATION_PREFERENCES** — How each user wants to be notified
```sql
NOTIF_ID        INT (PK, AUTO_INCREMENT)
USER_ID         INT (FK → USERS)
ALERT_ID        INT (FK → ALERT_MASTER)
NOTIFY_EMAIL    CHAR(1) DEFAULT 'N'
NOTIFY_SMS      CHAR(1) DEFAULT 'N'
PHONE_NUMBER    VARCHAR(15)
```

**Sample Data Tables** (for demo purposes)
- `ICMS_FAILURES` — Railway OHE failure records
- `FOOT_PATROL` — Foot patrol officer records
- `OHE_ASSETS` — OHE asset maintenance records
- `SPECIAL_DRIVES` — Ongoing special inspection drives

---

## Prerequisites

Make sure the following are installed before running:

| Tool | Version | Download |
|---|---|---|
| JDK | 21 | https://www.oracle.com/java/technologies/downloads/ |
| Apache Maven | 3.9+ | https://maven.apache.org/download.cgi |
| Apache Tomcat | 10.1 | https://tomcat.apache.org/download-10.cgi |
| MySQL | 8.0 | https://dev.mysql.com/downloads/installer/ |

### Verify Installations

```bash
java -version       # Should show 21.x.x
mvn -version        # Should show 3.x.x
mysql --version     # Should show 8.x.x
```

---

## Installation and Setup

### Step 1 — Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/CRISAlertDashboard.git
cd CRISAlertDashboard
```

### Step 2 — Set Up the Database

Open MySQL Workbench or MySQL CLI and run:

```sql
CREATE DATABASE tdms_alerts;
USE tdms_alerts;
```

Then create all tables:

```sql
CREATE TABLE ALERT_MASTER (
    ALERT_ID INT AUTO_INCREMENT PRIMARY KEY,
    ALERT_TITLE VARCHAR(255) NOT NULL,
    CARD_NAME VARCHAR(100) NOT NULL,
    COUNT_QUERY TEXT NOT NULL,
    DETAIL_QUERY TEXT NOT NULL,
    IS_ACTIVE CHAR(1) DEFAULT 'Y',
    CREATED_BY VARCHAR(100),
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE USERS (
    USER_ID INT AUTO_INCREMENT PRIMARY KEY,
    USERNAME VARCHAR(100) NOT NULL UNIQUE,
    PASSWORD VARCHAR(100) NOT NULL,
    FULL_NAME VARCHAR(200),
    EMAIL VARCHAR(200),
    PHONE_NUMBER VARCHAR(15),
    ROLE VARCHAR(50) DEFAULT 'USER',
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE USER_ALERT_PREFERENCES (
    PREF_ID INT AUTO_INCREMENT PRIMARY KEY,
    USER_ID INT NOT NULL,
    ALERT_ID INT NOT NULL,
    SELECTION_COUNT INT DEFAULT 0,
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID),
    FOREIGN KEY (ALERT_ID) REFERENCES ALERT_MASTER(ALERT_ID)
);

CREATE TABLE USER_NOTIFICATION_PREFERENCES (
    NOTIF_ID INT AUTO_INCREMENT PRIMARY KEY,
    USER_ID INT NOT NULL,
    ALERT_ID INT NOT NULL,
    NOTIFY_EMAIL CHAR(1) DEFAULT 'N',
    NOTIFY_SMS CHAR(1) DEFAULT 'N',
    PHONE_NUMBER VARCHAR(15),
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID),
    FOREIGN KEY (ALERT_ID) REFERENCES ALERT_MASTER(ALERT_ID)
);

CREATE TABLE ICMS_FAILURES (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    RAILWAY VARCHAR(10),
    DIVISION VARCHAR(20),
    DEPOT VARCHAR(20),
    FAILURE_START_DATE DATETIME,
    FAILURE_END_DATE DATETIME,
    SECTION VARCHAR(50),
    BLOCK_SECTION VARCHAR(50),
    REMARKS TEXT,
    STATUS VARCHAR(20) DEFAULT 'Un-assigned'
);

CREATE TABLE FOOT_PATROL (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    OFFICER_NAME VARCHAR(100),
    SECTION VARCHAR(50),
    PATROL_DATE DATE,
    START_TIME TIME,
    END_TIME TIME,
    DEFECTS_FOUND INT DEFAULT 0,
    STATUS VARCHAR(20)
);

CREATE TABLE OHE_ASSETS (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ASSET_CODE VARCHAR(50),
    ASSET_TYPE VARCHAR(100),
    SECTION VARCHAR(50),
    LOCATION VARCHAR(100),
    LAST_MAINTENANCE_DATE DATE,
    NEXT_MAINTENANCE_DATE DATE,
    MAINTENANCE_DONE TINYINT DEFAULT 0,
    STATUS VARCHAR(20)
);

CREATE TABLE SPECIAL_DRIVES (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    DRIVE_NAME VARCHAR(200),
    START_DATE DATE,
    END_DATE DATE,
    SECTION VARCHAR(50),
    OFFICER_INCHARGE VARCHAR(100),
    IS_ACTIVE TINYINT DEFAULT 1
);
```

Then insert sample data:

```sql
INSERT INTO USERS (USERNAME, PASSWORD, FULL_NAME, EMAIL, ROLE) VALUES
('admin', 'admin123', 'Admin User', 'admin@railway.com', 'ADMIN'),
('ashok', 'ashok123', 'Ashok Kumar Verma', 'ashok@railway.com', 'SENIOR'),
('renu', 'renu123', 'Renu Goyal', 'renu@railway.com', 'JUNIOR');

UPDATE USERS SET PHONE_NUMBER = '9999999999' WHERE USERNAME = 'ashok';
UPDATE USERS SET PHONE_NUMBER = '8888888888' WHERE USERNAME = 'renu';

INSERT INTO ALERT_MASTER (ALERT_TITLE, CARD_NAME, COUNT_QUERY, DETAIL_QUERY, IS_ACTIVE, CREATED_BY) VALUES
('No SOS found in last 7 days', 'SOS & SMS', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('SMS in last 15 days', 'SOS & SMS', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('ICMS Failures in last 45 days', 'Information Alerts', 'SELECT COUNT(*) FROM ICMS_FAILURES', 'SELECT RAILWAY, DIVISION, DEPOT, FAILURE_START_DATE, FAILURE_END_DATE, SECTION, BLOCK_SECTION, REMARKS, STATUS FROM ICMS_FAILURES', 'Y', 'Admin'),
('Today block Requests', 'Information Alerts', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('OHE tripping in last 30 days', 'Information Alerts', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('Current Special Drives', 'Information Alerts', 'SELECT COUNT(*) FROM SPECIAL_DRIVES WHERE IS_ACTIVE = 1', 'SELECT DRIVE_NAME, START_DATE, END_DATE, SECTION, OFFICER_INCHARGE FROM SPECIAL_DRIVES WHERE IS_ACTIVE = 1', 'Y', 'Admin'),
('ICMS asset failure pending to complete investigation', 'Working Alerts', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('Pending RDSO Guidelines for acknowledgement', 'Working Alerts', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('Contact wire span has more than 10 splices', 'OHE Use Cases', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('Catenary wire span has more than 10 splices', 'OHE Use Cases', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('Leaning masts', 'OHE Use Cases', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('Main Line OHE Mast having Critical Implantation', 'OHE Use Cases', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('Yard Line OHE Mast having Critical Implantation', 'OHE Use Cases', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('Sec/Stn wise Defect Count', 'Maintenance Information', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('OHE Assets with No Maintenance done', 'Maintenance', 'SELECT COUNT(*) FROM OHE_ASSETS WHERE MAINTENANCE_DONE = 0', 'SELECT ASSET_CODE, ASSET_TYPE, SECTION, LOCATION, LAST_MAINTENANCE_DATE, NEXT_MAINTENANCE_DATE, STATUS FROM OHE_ASSETS WHERE MAINTENANCE_DONE = 0', 'Y', 'Admin'),
('PSI Assets with No Maintenance done', 'Maintenance', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('OHE Assets Overdue for Maintenance', 'Maintenance', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('PSI Assets Overdue for Maintenance', 'Maintenance', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('Cantilever pending for updating Steady Arm Type', 'TDMS System', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('OHE Assets not Confirmed', 'TDMS System', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('Persons Live on Foot Patrol', 'Foot Patrol', 'SELECT COUNT(*) FROM FOOT_PATROL WHERE STATUS = "In Progress"', 'SELECT OFFICER_NAME, SECTION, PATROL_DATE, START_TIME, END_TIME, DEFECTS_FOUND, STATUS FROM FOOT_PATROL', 'Y', 'Admin'),
('Foot Patrol Defects Reported Today', 'Foot Patrol', 'SELECT COUNT(*) FROM FOOT_PATROL WHERE DEFECTS_FOUND > 0', 'SELECT OFFICER_NAME, SECTION, PATROL_DATE, DEFECTS_FOUND, STATUS FROM FOOT_PATROL WHERE DEFECTS_FOUND > 0', 'Y', 'Admin'),
('Foot Patrol Sections Overdue', 'Foot Patrol', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin'),
('PSI Locations Thermography reported critical', 'PSI Use Cases', 'SELECT COUNT(*) FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'SELECT * FROM ALERT_MASTER WHERE IS_ACTIVE = "Y"', 'Y', 'Admin');

INSERT INTO ICMS_FAILURES VALUES
(1, 'ER', 'HWH', 'HWH', '2026-04-29 20:52', '2026-04-29 21:55', 'BDC-KWAE Jn', 'BDC', 'OHE WIRE HANGING REPORTED BY LP OF 13018 DN', 'Un-assigned'),
(2, 'ER', 'HWH', 'HWH', '2026-05-01 22:32', '2026-05-02 00:48', 'NHT-BDAG', 'PKR', 'BETWEEN NHT TO PKR FREQUENT POWER TRIPPING', 'Un-assigned'),
(3, 'ER', 'HWH', 'HWH', '2026-05-03 11:40', '2026-05-03 11:48', 'BLY-SKG', 'NKL', 'AS PER SM NKL REPORTED FREQUENT OHE TRIPPING', 'Un-assigned'),
(4, 'ER', 'HWH', 'HWH', '2026-05-04 16:25', '2026-05-04 17:57', 'HWH-BLY', 'LLH', 'INFORMATION RECEIVED AT 16.25 HRS', 'Un-assigned'),
(5, 'ER', 'HWH', 'HWH', '2026-05-07 18:50', '2026-05-07 19:10', 'Bally-Bandel', 'RJS', 'AT RJS OHE TRIPPING FROM 18.50 HRS', 'Un-assigned');

INSERT INTO FOOT_PATROL VALUES
(1, 'Rajesh Kumar', 'BDC-KWAE', '2026-06-12', '06:00', '09:00', 2, 'Completed'),
(2, 'Amit Singh', 'NHT-BDAG', '2026-06-12', '07:00', '10:00', 0, 'Completed'),
(3, 'Suresh Yadav', 'BLY-SKG', '2026-06-12', '08:00', '11:00', 1, 'In Progress'),
(4, 'Mahesh Gupta', 'HWH-BLY', '2026-06-12', '09:00', '12:00', 0, 'Pending'),
(5, 'Dinesh Sharma', 'SKG-NKL', '2026-06-12', '10:00', '13:00', 3, 'Completed');

INSERT INTO OHE_ASSETS VALUES
(1, 'OHE-001', 'Contact Wire', 'BDC-KWAE', 'KM 25+400', '2026-01-15', '2026-04-15', 0, 'Overdue'),
(2, 'OHE-002', 'Catenary Wire', 'NHT-BDAG', 'KM 32+200', '2026-02-20', '2026-05-20', 0, 'Overdue'),
(3, 'OHE-003', 'Mast', 'BLY-SKG', 'KM 18+600', '2026-03-10', '2026-06-10', 0, 'Due'),
(4, 'OHE-004', 'Dropper', 'HWH-BLY', 'KM 45+100', '2026-04-05', '2026-07-05', 1, 'OK'),
(5, 'OHE-005', 'Steady Arm', 'SKG-NKL', 'KM 12+800', '2026-05-01', '2026-08-01', 1, 'OK');

INSERT INTO SPECIAL_DRIVES VALUES
(1, 'OHE Inspection Drive', '2026-06-01', '2026-06-30', 'HWH Division', 'SSE/TRD/HWH', 1),
(2, 'Mast Painting Drive', '2026-06-10', '2026-06-25', 'BDC-KWAE', 'JE/TRD/BDC', 1),
(3, 'Dropper Replacement Drive', '2026-06-05', '2026-06-20', 'NHT-BDAG', 'SSE/TRD/NHT', 1);
```

### Step 3 — Configure Database Connection

Open `src/main/resources/db.properties` and update with your MySQL credentials:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/tdms_alerts
db.username=root
db.password=YOUR_MYSQL_PASSWORD
```

### Step 4 — Configure Email (Optional — for L3 notifications)

Open `src/main/java/service/EmailService.java` and update:

```java
private static final String FROM_EMAIL = "yourgmail@gmail.com";
private static final String PASSWORD = "your_16_char_app_password";
```

To generate a Gmail App Password:
1. Go to myaccount.google.com → Security
2. Enable 2-Step Verification
3. Search "App Passwords" → Generate for Mail
4. Copy the 16 character password

### Step 5 — Build the Project

```bash
cd CRISAlertDashboard
mvn clean install
```

A `CRISAlertDashboard-1.0.war` file will be created in the `target/` folder.

### Step 6 — Deploy to Tomcat

Copy the WAR file to Tomcat's webapps folder:

```bash
copy target\CRISAlertDashboard-1.0.war C:\path\to\tomcat\webapps\
```

---

## Running the Application

### Start Tomcat

```bash
cd C:\path\to\tomcat\bin
.\startup.bat
```

### Open in Browser

```
http://localhost:8080/CRISAlertDashboard-1.0/login
```

### Stop Tomcat

```bash
.\shutdown.bat
```

### One-Click Run (Windows)

Double click `run.bat` in the project root — it builds, deploys, and starts Tomcat automatically.

---

## Usage Guide

### For Regular Users:
1. **Login** with your credentials
2. **Select Alerts** — choose which alerts you want on your dashboard
3. **View Dashboard** — see live counts for your selected alerts
4. **Click any alert** — popup opens showing full detail table
5. **Set Notifications** — go to Notifications to set email alerts per item
6. **Logout** when done

### For Admin Users:
1. Login with admin credentials
2. Click **Admin Panel** in the top navigation bar
3. **Add new alert** — fill in title, card, count query, detail query
4. **Activate/Deactivate** any alert with one click
5. New alerts appear on all users' preference pages immediately

---

## Default Credentials

| Username | Password | Role |
|---|---|---|
| admin | admin123 | Admin |
| ashok | ashok123 | Senior Officer |
| renu | renu123 | Junior Officer |

---

## Architecture

```
Browser
  ↓ HTTP Request
Servlet (Request Handler)
  ↓ calls
DAO (Database Access Object)
  ↓ JDBC
MySQL Database
  ↓ returns data
DAO → Servlet
  ↓ forwards
JSP (View)
  ↓ HTML Response
Browser

Background:
AppContextListener → AlertScheduler (every 30 min)
                          ↓
                    NotificationDAO
                          ↓
                    EmailService → Gmail SMTP → User Inbox
```
