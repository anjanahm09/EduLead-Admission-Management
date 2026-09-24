# EduLead – Admission Lead Management System

## Problem
Manage admission enquiries from first contact through follow-up and conversion.

## Features
- Lead creation, viewing, editing, deletion and search
- Admission lifecycle statuses
- Lead sources
- Counsellor assignment
- Follow-up scheduling and tracking
- Lead ageing
- Dashboard metrics
- Pipeline and source reports
- MySQL persistence
- REST APIs

## Technology
- Java 21
- Spring Boot
- Spring Data JPA / Hibernate
- MySQL
- HTML, CSS, JavaScript
- Maven
- Postman

## Architecture
Frontend → REST API → Controller → Service → Repository → MySQL

## Setup
1. Create/open a MySQL server.
2. Open `src/main/resources/application.properties`.
3. Replace `YOUR_MYSQL_PASSWORD` with the MySQL root password.
4. Open the project in IntelliJ IDEA.
5. Reload Maven.
6. Run `EduleadApplication`.
7. Confirm the console shows Tomcat on port 8080.
8. Open `frontend/index.html` in Chrome.

## API
- GET /api/leads
- GET /api/leads/{id}
- POST /api/leads
- PUT /api/leads/{id}
- DELETE /api/leads/{id}
- GET /api/followups
- GET /api/followups/lead/{leadId}
- GET /api/followups/today
- POST /api/followups
- PUT /api/followups/{id}
- DELETE /api/followups/{id}
- GET /api/dashboard

## Assumptions
- Roles are represented conceptually through counsellor/manager-oriented workflow in this prototype.
- Ageing thresholds: 0–2 days normal, 3–7 days ageing, 8+ days attention required.
- Lead sources include website, walk-in, phone, WhatsApp, education fair and campaign.
- Follow-up actions include call, WhatsApp, email and meeting.
- A lead must exist before a follow-up can be created.

## Edge cases considered
- Invalid lead ID for follow-up
- Missing required lead/follow-up fields
- Deleting a lead
- Empty search results
- Lead ageing
- Follow-up status tracking

## Validation
CRUD and follow-up APIs can be tested through Postman. The browser frontend consumes the same REST APIs.

## Simplified Run Instructions

The frontend is also packaged inside Spring Boot under `src/main/resources/static`, so the application can be run from one URL.

1. Configure the MySQL username and password in `src/main/resources/application.properties`.
2. Start `EduleadApplication` from IntelliJ IDEA or run the Maven Spring Boot application.
3. Open `http://localhost:8080/` in a browser.

No separate frontend server is required.
