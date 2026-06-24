# API Specification v1.0

Base URL:

/api

---

## Authentication APIs

### POST /auth/register

Register a new user.

Request:

{
"name": "Harsha",
"email": "[harsha@gmail.com](mailto:harsha@gmail.com)",
"password": "password123"
}

Response:

{
"message": "Registration successful"
}

---

### POST /auth/login

Request:

{
"email": "[harsha@gmail.com](mailto:harsha@gmail.com)",
"password": "password123"
}

Response:

{
"token": "jwt_token",
"userId": 1
}

---

## Profile APIs

### GET /profile

Returns logged-in user profile.

### PUT /profile

Update profile information.

### POST /profile/skills

Add user skill.

### DELETE /profile/skills/{id}

Delete skill.

---

## Resume APIs

### POST /resume/upload

Upload resume file.

Content-Type:

multipart/form-data

Response:

{
"resumeId": 1
}

---

### GET /resume/history

Get previous resume analyses.

---

### GET /resume/report/{id}

Get detailed ATS report.

---

## Interview APIs

### POST /interview/start

Request:

{
"companyId": 1,
"role": "Software Engineer",
"difficulty": "Medium"
}

Response:

{
"sessionId": 1,
"question": "Tell me about yourself."
}

---

### POST /interview/answer

Request:

{
"sessionId": 1,
"answer": "My name is Harsha..."
}

Response:

{
"score": 8,
"feedback": "Good answer..."
}

---

### GET /interview/report/{id}

Returns complete interview report.

---

## Company APIs

### GET /companies

Returns all companies.

### GET /company/{id}

Returns company details and resources.
