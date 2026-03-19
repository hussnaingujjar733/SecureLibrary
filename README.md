# SecureLibrary — SSDLC Final Project

A secure mini Library Management Web Application built with Java Spring Boot,
demonstrating secure software development lifecycle (SSDLC) practices.

---

## Technologies

| Layer | Technology |
|---|---|
| Backend | Java 17 + Spring Boot 3.2 |
| Security | Spring Security 6 |
| ORM | Spring Data JPA + Hibernate |
| Database | H2 (in-memory) |
| Templates | Thymeleaf |
| Build | Maven |
| SAST | SonarLint |
| DAST | OWASP ZAP |

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps

```bash
# 1. Clone / unzip the project
cd library-app

# 2. Build
mvn clean install -DskipTests

# 3. Run
mvn spring-boot:run
```

### Access the app
Open your browser at: **http://localhost:8080**

---

## Default Test Accounts

| Username | Password | Role |
|---|---|---|
| `hussnain` | `User@1234` | User |
| `admin` | `Admin@1234` | Librarian |

> Passwords are stored as BCrypt hashes — never in plain text.

---

## Security Features Implemented

| Feature | Implementation |
|---|---|
| Password hashing | BCrypt (strength 12) |
| Input validation | Jakarta Validation + Thymeleaf maxlength |
| Role-based access control | Spring Security `@PreAuthorize` + `hasRole()` |
| SQL injection prevention | Spring Data JPA parameterized JPQL queries |
| CSRF protection | Spring Security CSRF tokens (enabled by default) |
| XSS prevention | Thymeleaf auto-escaping on all `th:text` outputs |
| No stack traces | `server.error.include-stacktrace=never` + custom error page |
| No hardcoded secrets | All credentials in `application.properties` (not in code) |
| Security headers | CSP, Referrer-Policy, X-Frame-Options via Spring Security |
| Secure session | `HttpOnly`, `SameSite`, session invalidation on logout |
| Logging | SLF4J logs for login, registration, borrow actions, admin ops |

---

## Application Structure

```
src/main/java/com/ssdlc/library/
├── config/
│   ├── SecurityConfig.java       ← Spring Security rules
│   ├── CustomUserDetailsService  ← Loads users for authentication
│   └── DataSeeder.java           ← Seeds demo data on startup
├── controller/
│   ├── AuthController.java       ← Login / Register
│   ├── BookController.java       ← Book list, search, borrow
│   ├── AdminController.java      ← LIBRARIAN-only operations
│   └── CustomErrorController.java← Safe error pages
├── model/
│   ├── User.java                 ← USER / LIBRARIAN roles
│   ├── Book.java
│   └── BorrowRequest.java
├── repository/                   ← Spring Data JPA (parameterized queries)
└── service/                      ← Business logic + audit logging
```

---

## H2 Console (dev only)
Access at: **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:librarydb`
- Username: `sa` / Password: *(empty)*
