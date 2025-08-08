<h1 align="center" id="title">springboot-test-javan-backend</h1>

<p align="center">
  <strong>Test case project with login system</strong>
</p>

---

## 🌟 Project Overview

- **User Authentication 🔐:** Secure login with JWT access and refresh tokens.

---

## 🔄 Authentication Flow Check

Flow for testing authentication:

- **`test-access`**  
  Endpoint to verify access token. Require valid Bearer token. Check token valid or invalid.

- **`login`**  **`(username = user, password = user)`**  
  Endpoint to authenticate user get access token and refresh token. Use the access token to access `test-access`, and the refresh token to `refresh-token`.

- **`refresh-token`**  
  Endpoint to get new access and refresh token using a valid refresh token.

---

## 🌐 REST API Overview (Swagger UI)

**After Run Project :**
```
http://localhost:8080/swagger-ui/index.html
```

---

## ✅ Unit Test Overview

<img width="683" height="299" alt="unit-test-test-javan" src="https://github.com/user-attachments/assets/28798cac-9442-4149-bb09-e69751b048e9" />

---

## ⚙️ Technologies Used

- **Backend 💻:** Java 21 with Spring Boot 3  
- **Database 🗄️:** H2 (in-memory database) for local testing  
- **ORM 🛠️:** JPA with Hibernate (Java Persistence API)  
- **Database Migration 🛠️:** Flyway for database schema migrations  
- **Security 🔐:** Spring Security with JWT (JSON Web Token) for authentication 
- **Testing 🧪:** JUnit and Mockito for unit testing and mocking  
- **API Documentation 📜:** Spring Doc OpenAPI

<h2>🛠️ Installation Steps :</h2>

<p>1. Clone Repository</p>

```
git clone https://github.com/mdzakied/springboot-test-javan-backend
```

<br />
<p>2. Build the project using Maven Wrapper</p>

<br />

> [!NOTE]  
> * This project uses **Java 21** and Spring Boot 3. Make sure your local environment supports Java 21 before building and running the project.  

<br />

On Linux/macOS
```
./mvnw clean install
```

On Windows (PowerShell)
```
.\mvnw.cmd clean install
```

<br />
<p>3. Run the application</p>

On Linux/macOS
```
./mvnw spring-boot:run
```

On Windows (PowerShell)
```
.\mvnw.cmd spring-boot:run
```

<br />
<p>4. Run the test application (opsional).</p>

On Linux/macOS
```
./mvnw test
```

On Windows (PowerShell)
```
.\mvnw.cmd test
```
