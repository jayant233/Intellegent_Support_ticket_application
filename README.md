# 🎫 Intelligent Support Ticket System

A full-stack Java web application for **automated support ticket classification and lifecycle management**. Built with **Spring Boot**, **Thymeleaf**, **Direct JDBC**, and **MySQL**, and tested end-to-end with **Microsoft Playwright** and **JUnit 5**.

The application streamlines IT helpdesk workflows by automatically analyzing user-submitted problem descriptions using a keyword-based classification engine, determining the **issue category, severity, priority code, and SLA resolution target**, and persisting tickets using direct JDBC queries.

---

## 📋 Table of Contents

- [Problem Statement](#problem-statement)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Keyword Classification & SLA Engine](#keyword-classification--sla-engine)
- [Database Schema](#database-schema)
- [Getting Started](#getting-started)
- [Running Tests](#running-tests)
- [Core Java & Architectural Concepts Demonstrated](#core-java--architectural-concepts-demonstrated)
- [SQL Concepts Demonstrated](#sql-concepts-demonstrated)
- [Playwright Automation Concepts Demonstrated](#playwright-automation-concepts-demonstrated)
- [Author](#author)

---

## 📌 Problem Statement

In customer support and IT helpdesk environments, manually sorting, prioritizing, and assigning incoming support tickets creates bottlenecks and delays incident response times. 

This application provides an intelligent, automated solution:
1. Users submit their issue through a clean web portal.
2. The internal business logic instantly classifies the problem based on natural text keywords.
3. Appropriate severity (e.g., *Critical*, *High*), priority codes (*P1*, *P2*, *P3*, *P4*), and Service Level Agreement (SLA) deadlines are assigned in real-time.
4. Support teams and administrators can track, filter, search, and update ticket progress across their full lifecycle.

---

## ✨ Features

### 👤 User Portal (`/`)
- **Ticket Submission**: Users provide their name and problem description.
- **Automated Processing**: Instantly generates a unique ticket ID (e.g., `TKT1001`) and triggers classification.
- **Real-time Feedback**: Displays the generated Ticket ID upon submission.

### 🔎 Status Tracking Portal (`/status`)
- **Search by Ticket ID**: Allows customers to view the current status of their support request.
- **Detailed View**: Displays assigned category, priority, severity level, SLA hours, and live status (*Open*, *In Progress*, *Resolved*, *Closed*).

### 🛠️ Admin Dashboard (`/admin`)
- **Tabular Ticket Management**: View all submitted tickets in an organized table.
- **Multi-criteria Filtering**: Filter tickets dynamically by severity, priority, or status.
- **Direct Search**: Search directly for a specific Ticket ID.
- **Status Updates**: Update the lifecycle status of any ticket with immediate persistence.

---

## 🛠️ Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 17+ (or 11+) |
| **Framework** | Spring Boot 2.7.x / 3.x |
| **Web & Routing** | Spring MVC (`@Controller`, `@GetMapping`, `@PostMapping`) |
| **Templating** | Thymeleaf (`templates/user.html`, `status.html`, `admin.html`) |
| **Database** | MySQL 8.x |
| **Persistence** | Direct JDBC (`DriverManager`, `PreparedStatement`, `ResultSet`) |
| **Build Tool** | Apache Maven |
| **Testing** | JUnit 5, Microsoft Playwright for Java |

---

## 🏗️ Architecture

The application follows a clean, interview-friendly **Controller → Service → DAO → JDBC → MySQL** architecture with distinct separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                           Browser                           │
│        (user.html / status.html / admin.html via Thymeleaf) │
└──────────────────────────────┬──────────────────────────────┘
                               │  HTTP GET / POST
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                    TicketController.java                    │
│     @GetMapping("/")          @PostMapping("/ticket")       │
│     @GetMapping("/status")    @PostMapping("/status")       │
│     @GetMapping("/admin")     @PostMapping("/admin/*")      │
└──────────────────────────────┬──────────────────────────────┘
                               │  Coordinates Business Flow
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                      TicketService.java                     │
│    createTicket() · getTicketById() · getAllTickets() ...   │
│     ├── TicketIdGenerator.java                              │
│     └── KeywordClassifierService.java                       │
└──────────────────────────────┬──────────────────────────────┘
                               │  Calls Data Access Layer
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                       TicketDAO.java                        │
│   saveTicket() · getTicketById() · updateTicketStatus() ... │
└──────────────────────────────┬──────────────────────────────┘
                               │  JDBC (PreparedStatement)
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                  MySQL (support_ticket_db)                  │
│                        tickets table                        │
└─────────────────────────────────────────────────────────────┘
```

### Layer Responsibilities:
- **Controller (`TicketController.java`)**: Handles HTTP requests, reads form inputs, interacts with the Service, and renders Thymeleaf views.
- **Service (`TicketService.java`)**: Executes business rules (ticket creation workflow, coordinating classification with persistence).
- **Classification Engine (`KeywordClassifierService.java`)**: Analyzes text to determine Category, Severity, Priority, and SLA hours.
- **DAO (`TicketDAO.java`)**: Manages raw SQL execution, connection lifecycle, `PreparedStatement` parameters, and safe resource closure.
- **Database**: Stores ticket records in MySQL.

---

## 🗂️ Project Structure

```
INTELLEGENT/
│
├── pom.xml                                        # Maven configuration
├── README.md                                      # Project documentation
│
├── database/
│   └── schema.sql                                 # MySQL table DDL and seed data
│
├── src/main/java/com/support/ticket/
│   ├── SupportTicketApplication.java              # Spring Boot main entry point
│   ├── Ticket.java                                # POJO model for support tickets
│   ├── TicketController.java                      # Unified web controller (User & Admin routes)
│   ├── TicketService.java                         # Business logic & workflow coordinator
│   ├── TicketDAO.java                             # Direct JDBC data access object
│   ├── KeywordClassifierService.java              # Keyword-to-category & SLA classifier
│   ├── TicketIdGenerator.java                     # Thread-safe Ticket ID generator
│   ├── TicketNotFoundException.java               # Custom exception for missing tickets
│   └── DatabaseException.java                     # Custom runtime exception for SQL errors
│
├── src/main/resources/
│   ├── templates/
│   │   ├── user.html                              # Ticket submission view
│   │   ├── status.html                            # Ticket status lookup view
│   │   └── admin.html                             # Admin dashboard & management view
│   ├── static/
│   │   └── css/                                   # Application stylesheets
│   └── application.properties                     # Spring & server settings
│
└── src/test/java/playwright/
    ├── BasePlaywrightTest.java                    # Base test with browser setup & teardown
    ├── SubmitTicketPlaywrightTest.java            # TC01–TC04: Submission & field validation
    ├── CheckStatusPlaywrightTest.java             # TC05–TC07: Status search & error validation
    └── AdminDashboardPlaywrightTest.java          # TC08–TC10: Dashboard display, filters & updates
```

---

## 🧠 Keyword Classification & SLA Engine

The `KeywordClassifierService` evaluates user descriptions and applies the following rule matrix:

| Issue Category | Trigger Keywords / Phrases | Severity | Priority | SLA Target |
| :--- | :--- | :--- | :--- | :--- |
| **Login Issue** | `login`, `signin`, `sign in`, `authentication`, `account access`, `unable to login` | Medium | P3 | 8 Hours |
| **Outage Issue** | `outage`, `down`, `offline`, `downtime`, `service unavailable`, `system down` | Critical | P1 | 1 Hour |
| **Password Issue** | `password`, `forgot password`, `reset password`, `password change`, `incorrect password`, `password expired` | Low | P4 | 24 Hours |
| **Payment Issue** | `payment`, `billing`, `invoice`, `charge`, `credit card`, `refund` | High | P2 | 4 Hours |
| **Performance Issue** | `slow`, `performance`, `lag`, `hanging`, `freezing`, `latency` | Medium | P3 | 12 Hours |
| **Application Error** | `error`, `exception`, `crash`, `bug`, `stacktrace`, `internal error` | High | P2 | 3 Hours |
| **Database Issue** | `database`, `sql`, `connection pool`, `data loss`, `corruption`, `timeout` | Critical | P1 | 2 Hours |
| **UI Bug** | `ui`, `button`, `layout`, `alignment`, `rendering`, `display` | Low | P4 | 48 Hours |
| **Access Issue** | `access denied`, `forbidden`, `permissions`, `unauthorized`, `role`, `locked out` | High | P2 | 6 Hours |
| **Email Issue** | `email`, `spam`, `not receiving`, `bounce`, `smtp`, `delivery` | Medium | P3 | 10 Hours |
| **General Support** | *(Fallback when no specific keyword matches)* | Low | P4 | 48 Hours |

---

## 🗄️ Database Schema

The `database/schema.sql` creates the `tickets` table:

```
┌───────────────────────────────────────────────┐
│                    tickets                    │
├───────────────────────────────┬───────────────┤
│ ticket_id (PK)                │ VARCHAR(50)   │
│ customer_name                 │ VARCHAR(100)  │
│ description                   │ TEXT          │
│ category                      │ VARCHAR(100)  │
│ severity                      │ VARCHAR(50)   │
│ priority                      │ VARCHAR(50)   │
│ status                        │ VARCHAR(50)   │
│ sla_hours                     │ INT           │
└───────────────────────────────┴───────────────┘
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 17+** (or Java 11+)
- **Maven 3.8+**
- **MySQL 8.x** running locally

### 1. Set Up the Database

Log in to MySQL and run the schema script:

```bash
mysql -u root -p < database/schema.sql
```

### 2. Configure Database Credentials

Open `src/main/java/com/support/ticket/TicketDAO.java` and confirm/update the connection constants:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/support_ticket_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true";
private static final String USER     = "root";
private static final String PASSWORD = "password";
```

### 3. Build & Run the Web Application

```bash
mvn spring-boot:run
```

Once running, navigate to:
- **User Portal:** [http://localhost:8080/](http://localhost:8080/)
- **Status Check:** [http://localhost:8080/status](http://localhost:8080/status)
- **Admin Dashboard:** [http://localhost:8080/admin](http://localhost:8080/admin)

---

## 🧪 Running Tests

The application uses **Microsoft Playwright for Java** combined with **JUnit 5** for automated UI testing.

### Run All Playwright UI Tests

1. Start the application in one terminal:
   ```bash
   mvn spring-boot:run
   ```
2. In a second terminal, execute the test suite:
   ```bash
   mvn test
   ```

### Run a Specific Test Class

```bash
mvn test -Dtest=SubmitTicketPlaywrightTest
mvn test -Dtest=CheckStatusPlaywrightTest
mvn test -Dtest=AdminDashboardPlaywrightTest
```

### Test Coverage Matrix

| Test ID | Test Class | Validated Behavior |
| :--- | :--- | :--- |
| **TC01** | `SubmitTicketPlaywrightTest` | Submit ticket page loads and heading is visible |
| **TC02** | `SubmitTicketPlaywrightTest` | Form input fields and submit button are displayed |
| **TC03** | `SubmitTicketPlaywrightTest` | Successful ticket submission and Ticket ID generation |
| **TC04** | `SubmitTicketPlaywrightTest` | HTML5 mandatory field validation for empty submission |
| **TC05** | `CheckStatusPlaywrightTest` | Check status page elements render properly |
| **TC06** | `CheckStatusPlaywrightTest` | Search for a submitted ticket and verify details |
| **TC07** | `CheckStatusPlaywrightTest` | Validation and error display for non-existent Ticket ID |
| **TC08** | `AdminDashboardPlaywrightTest` | Admin dashboard loads and displays tickets table |
| **TC09** | `AdminDashboardPlaywrightTest` | Dropdown filtering by severity (e.g., Critical) |
| **TC10** | `AdminDashboardPlaywrightTest` | Administrator updates ticket status from Open → In Progress |

---

## 💡 Core Java & Architectural Concepts Demonstrated

| Concept | Location | Implementation Detail |
| :--- | :--- | :--- |
| **Controller-Service-DAO Pattern** | Entire Project | Clear separation of routing, business logic, and persistence |
| **Direct JDBC Resource Management** | `TicketDAO.java` | Explicit `try` / `catch` / `finally` blocks closing `ResultSet`, `PreparedStatement`, `Connection` |
| **Encapsulation & POJO Modeling** | `Ticket.java` | Private fields with public getters, setters, and parameterized constructors |
| **Custom Exceptions** | `TicketNotFoundException.java`, `DatabaseException.java` | Checked/unchecked exception handling across layers |
| **Rule Matching Engine** | `KeywordClassifierService.java` | Categorization using collections (`Map<String, List<String>>`) |
| **Thread Safety** | `TicketIdGenerator.java` | Atomic generation via `AtomicInteger` |

---

## 📊 SQL Concepts Demonstrated

- **DDL (`CREATE TABLE`, `DROP TABLE`)**: Table schema definitions with constraints.
- **DML (`INSERT`, `UPDATE`, `SELECT`)**: CRUD operations using parameterized `PreparedStatement`.
- **Dynamic Filtering (`WHERE 1=1 AND ...`)**: Programmatically appending SQL conditions for filtering.
- **Constraints**: `PRIMARY KEY`, `NOT NULL`, and data typing.

---

## 🎭 Playwright Automation Concepts Demonstrated

- **Modular Test Hierarchy**: Shared `@BeforeAll` and `@AfterAll` lifecycle via `BasePlaywrightTest`.
- **Role & Label Locators**: Robust targeting via `page.getByRole()`, `page.getByLabel()`, and `locator()`.
- **Fluent Assertions**: Using `assertThat()` for element visibility, text matching, and attribute checks.
- **Headless Browser Execution**: Cross-browser Chromium automation.

---

## 👤 Author

**Jayant**
- 💼 GitHub: [@jayant233](https://github.com/jayant233)
- 📧 Email: bjayant231@gmail.com
