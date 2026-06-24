# Database Schema v1.0

## users

| Field | Type |
|---------|---------|
| id | BIGINT PK |
| name | VARCHAR(100) |
| email | VARCHAR(100) UNIQUE |
| password | VARCHAR(255) |
| role | VARCHAR(50) |
| is_active | BOOLEAN |
| college | VARCHAR(150) |
| degree | VARCHAR(100) |
| branch | VARCHAR(100) |
| cgpa | DECIMAL(3,2) |
| graduation_year | INT |
| target_role | VARCHAR(100) |
| created_at | TIMESTAMP |
| updated_at | TIMESTAMP |
---

## skills

| Field | Type         |
| ----- | ------------ |
| id    | BIGINT PK    |
| name  | VARCHAR(100) |

---

## user_skills

| Field       | Type          |
| ----------- | ------------- |
| id          | BIGINT PK     |
| user_id     | FK(users.id)  |
| skill_id    | FK(skills.id) |
| proficiency | VARCHAR(50)   |

---

## companies

| Field       | Type         |
| ----------- | ------------ |
| id          | BIGINT PK    |
| name        | VARCHAR(100) |
| description | TEXT         |

---

## user_target_companies

| Field      | Type             |
| ---------- | ---------------- |
| id         | BIGINT PK        |
| user_id    | FK(users.id)     |
| company_id | FK(companies.id) |

---

## resumes

| Field       | Type         |
| ----------- | ------------ |
| id          | BIGINT PK    |
| user_id     | FK(users.id) |
| file_name   | VARCHAR(255) |
| file_path   | VARCHAR(500) |
| uploaded_at | TIMESTAMP    |

---

## resume_analysis

| Field           | Type           |
| --------------- | -------------- |
| id              | BIGINT PK      |
| resume_id       | FK(resumes.id) |
| ats_score       | INT            |
| strengths       | TEXT           |
| weaknesses      | TEXT           |
| recommendations | TEXT           |
| created_at      | TIMESTAMP      |

---

## interview_sessions

| Field         | Type             |
| ------------- | ---------------- |
| id            | BIGINT PK        |
| user_id       | FK(users.id)     |
| company_id    | FK(companies.id) |
| role          | VARCHAR(100)     |
| difficulty    | VARCHAR(50)      |
| overall_score | INT              |
| created_at    | TIMESTAMP        |

---

## interview_questions

| Field      | Type                      |
| ---------- | ------------------------- |
| id         | BIGINT PK                 |
| session_id | FK(interview_sessions.id) |
| question   | TEXT                      |
| answer     | TEXT                      |
| feedback   | TEXT                      |
| score      | INT                       |

---

## company_resources

| Field         | Type             |
| ------------- | ---------------- |
| id            | BIGINT PK        |
| company_id    | FK(companies.id) |
| title         | VARCHAR(255)     |
| content       | TEXT             |
| resource_type | VARCHAR(50)      |
| created_at    | TIMESTAMP        |
