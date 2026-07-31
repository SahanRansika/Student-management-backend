# 🎓 Student Management System - Backend

A secure REST API built with **Spring Boot**, **Spring Security**, **JWT Authentication**, and **MongoDB** for managing student records.

---

## 🚀 Features

- 🔐 JWT Authentication
- 👤 User Registration & Login
- 🎓 Student CRUD Operations
- 🔍 Search Students
- 🛡️ Spring Security
- 🌐 RESTful APIs
- 📦 MongoDB Integration
- ⚡ Exception Handling
- 📄 DTO Architecture

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3
- Spring Security
- JWT
- MongoDB
- Maven

---

## 📂 Project Structure

```
Backend
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── lk/srk/backend
│   │   │        ├── config
│   │   │        ├── controller
│   │   │        ├── dto
│   │   │        ├── model
│   │   │        ├── repository
│   │   │        ├── service
│   │   │        └── security
│   │   └── resources
│   │        └── application.properties
│   └── test
│
└── pom.xml
```

---

# ⚙️ Requirements

- Java 17+
- Maven
- MongoDB Atlas or Local MongoDB

---

# 📥 Installation

Clone Repository

```bash
git clone https://github.com/SahanRansika/Student-management-backend.git

cd Student-management-backend
```

---

## Configure MongoDB

Update

```
src/main/resources/application.properties
```

```properties
spring.data.mongodb.uri=YOUR_MONGODB_URI
spring.data.mongodb.database=studentdb

jwt.secret=YOUR_SECRET_KEY
jwt.expiration=86400000
```

---

## Run Application

```bash
mvn clean install

mvn spring-boot:run
```

Server runs at

```
http://localhost:8080
```

---

# 📡 REST API

## Authentication

| Method | Endpoint |
|---------|----------|
| POST | /api/auth/register |
| POST | /api/auth/login |
| GET | /api/auth/me |
| GET | /api/auth/check |

---

## Students

| Method | Endpoint |
|---------|----------|
| GET | /api/students |
| GET | /api/students/{id} |
| POST | /api/students |
| PUT | /api/students/{id} |
| DELETE | /api/students/{id} |
| GET | /api/students/search |
| GET | /api/students/count |

---

# 🧪 Testing

```bash
mvn test
```

---

# 📦 Build

```bash
mvn clean package
```

Run

```bash
java -jar target/Backend-0.0.1-SNAPSHOT.jar
```

---

# 🔒 Environment Variables

| Variable | Description |
|----------|-------------|
| spring.data.mongodb.uri | MongoDB URI |
| spring.data.mongodb.database | Database Name |
| jwt.secret | JWT Secret |
| jwt.expiration | JWT Expiration |

---

# 🛠 Common Errors

## MongoDB Connection Failed

- Check Atlas URI
- Check Username/Password
- Check Network Access

## Port Already in Use

Windows

```cmd
netstat -ano | findstr :8080
taskkill /PID PID /F
```

---

# 👨‍💻 Author

Sahan Ransika

GitHub

https://github.com/SahanRansika

Frontend Repository

https://github.com/SahanRansika/Student-management-frontend

---

⭐ Don't forget to Star this repository!
