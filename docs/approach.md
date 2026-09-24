# EduLead Approach Note

## 1. Problem Understanding
The system manages admission leads from multiple sources and supports the journey from first enquiry through follow-up, application and conversion.

## 2. Product Decisions
The prototype focuses on the highest-value operational workflow:
- capture lead
- assign counsellor
- track status
- schedule follow-up
- monitor ageing
- view dashboard and reports

## 3. Architecture
A simple layered Spring Boot backend is used:
Controller → Service → Repository → MySQL.

The frontend is a lightweight HTML/CSS/JavaScript client consuming REST APIs.

## 4. Database
Two core tables are used:
- leads
- follow_ups

The follow-up stores the lead ID so the workflow remains easy to understand and test.

## 5. Trade-offs
For a time-boxed prototype, course and counsellor are stored as text fields instead of separate master tables. This reduces implementation complexity while demonstrating the requested workflow. A production version could normalize these into course and user/counsellor tables.

## 6. Important Edge Cases
- follow-up cannot reference a non-existent lead
- empty search results
- invalid resource IDs
- lead ageing
- follow-up status changes
- duplicate or incomplete data should be validated further in production

## 7. Future Enhancements
- role-based authentication
- counsellor workload dashboard
- course master
- lead status history
- notification/reminder service
- payment/application integration
- stronger server-side validation and audit logs
