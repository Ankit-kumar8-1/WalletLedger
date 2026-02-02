


# WalletLedger

**WalletLedger** is a backend service for tracking personal income and expenses, built using **Spring Boot** and **MySQL**. It provides user management, financial tracking, reporting, and email notifications to enhance the user experience.

## Overview

WalletLedger helps users manage their personal finances by allowing them to track categories, incomes, and expenses. It secures APIs using **JWT authentication** and supports email notifications for account activation and financial summaries.

---

## Project Structure

```

WALLETLEDGER/SRC/MAIN/JAVA
└───in
    └───ankitsaahariya
        └───WalletLedger
            │   WalletLedgerApplication.java
            │   
            ├───config
            │       SecurityConfig.java
            │       
            ├───controller
            │       CategoryController.java
            │       DashboardController.java
            │       ExpenseController.java
            │       FilterController.java
            │       HomeController.java
            │       IncomeController.java
            │       ProfileController.java
            │       
            ├───dto
            │       AuthDto.java
            │       CategoryDTO.java
            │       ExpenseDTO.java
            │       FilterDTO.java
            │       IncomeDTO.java
            │       ProfileDto.java
            │       RecentTransactionDTO.java
            │       
            ├───entity
            │       CategoryEntity.java
            │       ExpenseEntity.java
            │       IncomeEntity.java
            │       ProfileEntity.java
            │       
            ├───repository
            │       CategoryRepository.java
            │       ExpenseRepository.java
            │       IncomeRepository.java
            │       ProfileRepository.java
            │       
            ├───security
            │       JwtAuthenticationFilter.java
            │       JwtUtil.java
            │       
            ├───services
            │       AppUserDetailsService.java
            │       CategoryService.java
            │       DashBoardService.java
            │       EmailService.java
            │       ExpenseService.java
            │       IncomeService.java
            │       NotificationServices.java
            │       ProfileService.java
            │       
            └───util

```

## Features

* User registration with email activation
* **JWT-based login** and stateless security
* Category CRUD (Income/Expense) with user scoping and type filtering
* Income and expense CRUD for authenticated users
* Transaction filtering and sorting
* Dashboard summary with totals and recent transactions
* **Daily scheduled email reminders** and summaries
* **Excel export** support using Apache POI

---

## Tech Stack

* **Java 21**
* **Spring Boot**
* **Spring Security + JWT**
* **Spring Data JPA**
* **MySQL**
* **Lombok**
* **Spring Boot Mail (JavaMail)**
* **Apache POI**

---

## Base API URL

```

[http://localhost:8080/api/v1.0](http://localhost:8080/api/v1.0)

```

---

## API Endpoints

### 🔐 Authentication APIs

| Method | URL                      | Description               | Role   |
|--------|--------------------------|---------------------------|--------|
| POST   | `/register`               | Register a new user       | PUBLIC |
| GET    | `/activate?token=...`     | Activate user account     | PUBLIC |
| POST   | `/login`                  | Login & generate JWT      | PUBLIC |

JWT must be sent as:

```

Authorization: Bearer <token>

```

---

### 📂 Category APIs

| Method | URL                      | Description                               | Role |
|--------|--------------------------|-------------------------------------------|------|
| POST   | `/categories`             | Create category (income/expense)          | USER |
| GET    | `/categories`             | Get all categories                        | USER |
| GET    | `/categories?type=income` | Get categories by type (income/expense)   | USER |
| PUT    | `/categories/{id}`        | Update category                           | USER |

---

### 💸 Expense APIs

| Method | URL             | Description       | Role |
|--------|-----------------|-------------------|------|
| POST   | `/expenses`      | Create expense    | USER |
| GET    | `/expenses`      | Read all expenses | USER |
| DELETE | `/expenses/{id}` | Delete expense    | USER |

---

### 💰 Income APIs

| Method | URL             | Description      | Role |
|--------|-----------------|------------------|------|
| POST   | `/incomes`      | Create income    | USER |
| GET    | `/incomes`      | Read all incomes | USER |
| DELETE | `/incomes/{id}` | Delete income    | USER |

---

### 📊 Dashboard API

| Method | URL         | Description                | Role |
|--------|-------------|----------------------------|------|
| GET    | `/dashboard`| Get dashboard summary data | USER |

---

### 🔍 Transaction Filter API

| Method | URL                       | Description                        | Role |
|--------|---------------------------|------------------------------------|------|
| POST   | `/transactions/filter`    | Filter income/expense with sorting | USER |

---

## Authentication Flow

1. User registers with email.
2. Activation token is generated.
3. An activation email is sent.
4. User activates the account.
5. User logs in and a JWT is returned.
6. JWT must be included in the header for all secured API requests.

---

## Scheduler & Email Jobs

| Job                                 | Description                                      |
|-------------------------------------|--------------------------------------------------|
| **Daily Income & Expense Reminder** | Reminds users to add daily transactions.         |
| **Daily Expense Summary**           | Sends a tabular summary of daily expenses.       |

Mail configuration is handled via properties or environment variables.

---

## How to Run Locally

1. Clone the repository.
2. Create a MySQL database.
3. Copy `application.properties.example` to `application.properties`.
4. Configure database and mail settings.
5. Run the application:

```

mvn spring-boot:run

````

Or directly from your IDE.

---

## `application.properties` Example

```properties
server.servlet.context-path=/api/v1.0

# Configure MySQL datasource and Hibernate JPA
spring.datasource.url=jdbc:mysql://localhost:3306/walletledgedb
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate/JPA Settings to auto create/update tables
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.use_sql_comments=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql=TRACE

# Mail settings
spring.mail.host=
spring.mail.port=
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.protocol=smtp
spring.mail.properties.mail.smtp.from=

# JWT token settings
jwt.secret=
jwt.expiration=

money.manager.frontend.url=http://localhost:5173
````

## Postman Collection
This project includes a Postman collection JSON file containing all API endpoints.
It can be imported into Postman to test and understand the API flow.
---

## License

**Personal and educational use only.**

---

## Contact

**Author:** Ankit

**Project:** WalletLedger

**Email:** [ankitk.software@gmail.com](mailto:ankitk.software@gmail.com)

**GitHub:** [https://github.com/Ankit-kumar8-1](https://github.com/Ankit-kumar8-1)

---


