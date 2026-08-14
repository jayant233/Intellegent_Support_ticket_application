# 🎫 Intelligent Support Ticket System

A beginner-friendly, web-based **support ticket management application** built with Java and Spring Boot. This project demonstrates full-stack Java development using Spring MVC, Thymeleaf templating, MySQL persistence, and JUnit 5 testing — making it an ideal portfolio piece for aspiring Java developers.

---

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [How to Run](#how-to-run)
- [Running Tests](#running-tests)
- [Screenshots](#screenshots)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

## 📌 Project Overview

The **Intelligent Support Ticket System** is a lightweight helpdesk application where users can submit, view, update, and resolve support tickets. It simulates a real-world IT support workflow, making it useful for learning Spring Boot fundamentals while building something practical and demonstrable.

---

## ✨ Features

- ✅ **Submit** new support tickets with title, description, and priority level
- 📋 **View** all tickets in a paginated, sortable list
- 🔍 **Search** tickets by keyword, status, or priority
- ✏️ **Update** ticket details and status (Open → In Progress → Resolved)
- 🗑️ **Delete** tickets with confirmation
- 🏷️ **Priority tagging** — Low, Medium, High, Critical
- 📊 **Status tracking** — Open, In Progress, Resolved, Closed
- 🔒 Basic input validation with user-friendly error messages

---

## 🛠️ Technology Stack

| Layer         | Technology                  |
|---------------|-----------------------------|
| Language      | Java 17+                    |
| Framework     | Spring Boot 3.x             |
| Web Layer     | Spring MVC                  |
| Templating    | Thymeleaf                   |
| Build Tool    | Maven                       |
| Database      | MySQL 8.x                   |
| ORM           | Spring Data JPA (Hibernate) |
| Frontend      | HTML5, CSS3                 |
| Testing       | JUnit 5, Spring Boot Test   |
| IDE           | Visual Studio Code          |

---

## 🗂️ Project Structure

```
INTELLEGENT/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/support/ticket/
│   │   │       ├── controller/       # Spring MVC Controllers
│   │   │       ├── model/            # Entity classes (Ticket, etc.)
│   │   │       ├── repository/       # Spring Data JPA Repositories
│   │   │       ├── service/          # Business logic layer
│   │   │       └── IntellegentApplication.java
│   │   └── resources/
│   │       ├── templates/            # Thymeleaf HTML templates
│   │       ├── static/               # CSS, JS, images
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/support/ticket/   # JUnit 5 test classes
├── pom.xml
├── .gitignore
└── README.md
```

---

## ✅ Prerequisites

Make sure you have the following installed before running the project:

- **Java JDK 17+** — [Download](https://adoptium.net/)
- **Apache Maven 3.8+** — [Download](https://maven.apache.org/download.cgi)
- **MySQL 8.x** — [Download](https://dev.mysql.com/downloads/)
- **VS Code** with the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/intelligent-support-ticket-system.git
cd intelligent-support-ticket-system
```

### 2. Configure the Database

Log in to MySQL and create the database:

```sql
CREATE DATABASE support_ticket_db;
```

### 3. Update `application.properties`

Open `src/main/resources/application.properties` and update your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/support_ticket_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Install Dependencies

```bash
mvn clean install
```

---

## ▶️ How to Run

```bash
mvn spring-boot:run
```

Once started, open your browser and navigate to:

```
http://localhost:8080
```

> The application will automatically create the required database tables on first run (via `ddl-auto=update`).

---

## 🧪 Running Tests

To run all JUnit 5 tests:

```bash
mvn test
```

To run a specific test class:

```bash
mvn -Dtest=TicketServiceTest test
```

Test reports are generated in:

```
target/surefire-reports/
```

---

## 📸 Screenshots

> *Screenshots will be added once the UI is finalized.*

| Page            | Preview                        |
|-----------------|--------------------------------|
| Home / Ticket List | *(coming soon)*             |
| Create Ticket   | *(coming soon)*                |
| Ticket Detail   | *(coming soon)*                |

---

## 🚀 Future Improvements

- [ ] User authentication and role-based access (Admin / Agent / User)
- [ ] Email notifications when ticket status changes
- [ ] File attachment support for tickets
- [ ] Dashboard with analytics and charts
- [ ] REST API endpoints for mobile/frontend integration
- [ ] Docker containerization for easy deployment
- [ ] Pagination and advanced filtering

---

## 👤 Author

**Jayant**

- 💼 GitHub: [@jayant233](https://github.com/jayant233)
- 📧 Email: bjayant231@gmail.com

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

> *Built as a learning project to demonstrate Java Spring Boot development skills.*
