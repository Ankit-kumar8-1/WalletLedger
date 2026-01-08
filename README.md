# WalletLedger

WalletLedger is a backend service for personal income and expense tracking.
It uses Spring Boot and MySQL.

# Overview

This project stores user profiles, categories, incomes, and expenses.
It secures APIs with JWT.
It sends activation and summary emails.
It runs scheduled jobs for daily reminders and reports.

# Features

* User registration with email activation.
* JWT based login and stateless security.
* Category CRUD with user scoping and type filters.
* Income and expense CRUD for current user.
* Transaction filters by date, keyword, and sort.
* Dashboard summary with totals and recent transactions.
* Daily scheduled email reminders and expense summary.
* Excel export support (Apache POI).

# Tech Stack

* Java 21.
* Spring Boot.
* Spring Security + JWT.
* Spring Data JPA.
* MySQL.
* Lombok.
* JavaMail (Spring Boot mail).
* Apache POI.

# Base API URL

Default base URL:

`http://localhost:8080/api/v1.0`

# API Structure (match Postman)

### Register / Activate User account /  Login  

* `POST /register`

  <img width="691" height="439" alt="register" src="https://github.com/user-attachments/assets/50e783d3-de2b-4580-b491-5fe7134f2503" />

* `GET /activate?token=...`
  
  
  <img width="819" height="130" alt="activate" src="https://github.com/user-attachments/assets/f44fb059-663b-417a-8294-af9d625d3628" /> 

  <img width="733" height="114" alt="activate2" src="https://github.com/user-attachments/assets/9f5e6490-9be5-406d-9570-29bf7ab3184e" />


* `POST /login`
  

   <img width="966" height="475" alt="Login" src="https://github.com/user-attachments/assets/09a7eaa6-c1ab-461b-a35f-4b59db911929" />  
 


* `Authorization (JWT Bearer Token)`

   The JWT token is received after a successful login.
   Paste this token in Postman under Authorization → Bearer Token.


   <img width="676" height="293" alt="Bearer Token " src="https://github.com/user-attachments/assets/2d8b5629-a86b-48c6-9d5c-db3ff3f221c2" />


### Create category / Update / Read / Read by type

* `POST /categories` (Create Category)


  
  <img width="965" height="414" alt="Create Category 1" src="https://github.com/user-attachments/assets/53b1d418-3566-4ee8-a78a-ef967aa16beb" />


  <img width="962" height="409" alt="create Category 2" src="https://github.com/user-attachments/assets/9efd69e5-f3fe-45c0-b1f8-73fef4e3b6d0" />


* `GET /categories` (Read Category)


	
  <img width="972" height="542" alt="Read categories " src="https://github.com/user-attachments/assets/546049f3-a42f-4356-8c8b-82df2908ea7a" />

* `GET /categories?type=expense|income` (Read categories by type)


  <img width="977" height="384" alt="Read category by type" src="https://github.com/user-attachments/assets/b7d8e162-877e-48aa-8a86-4543f7d6f2ea" />


* `PUT /categories/{id}` (Update Category)


  <img width="975" height="406" alt="Update category by ID" src="https://github.com/user-attachments/assets/25cf0238-92f1-445d-82eb-5c2659a0cf54" />

  

### Expenses


* `POST /expenses` (Create Expense)
  

  <img width="973" height="469" alt="Expenses 1" src="https://github.com/user-attachments/assets/dad22051-2c6f-4361-9141-4fd79b049995" />
  

* `GET /expenses` (Read Expenses)

  <img width="987" height="426" alt="Read Expenses" src="https://github.com/user-attachments/assets/65c69edc-425e-4d72-b56e-3c81cd90fd0a" />



* `DELETE /expenses/{id}` (Delete Expense)

  <img width="974" height="232" alt="Delete Expense" src="https://github.com/user-attachments/assets/c1c0a12e-057b-4b07-bcd1-22cb57e9578f" />


### Incomes

* `POST /incomes` (Create Income)
  
  <img width="975" height="454" alt="Income " src="https://github.com/user-attachments/assets/06fe94b1-7f3b-4c24-8678-b7fd24bc970d" />

  
* `GET /incomes` (Read Incomes)

  <img width="965" height="533" alt="Read Incomes" src="https://github.com/user-attachments/assets/c921007f-f594-4095-af30-bab0c6090b23" />


* `DELETE /incomes/{id}` (Delete Income)
  
  <img width="972" height="308" alt="Delete Income" src="https://github.com/user-attachments/assets/c5044d6b-653d-41c6-b111-45cea2a8a701" />


### Read Dashboard Data

* `GET /dashboard` (Dashboard Data)

  <img width="1071" height="590" alt="Read dashboard 1" src="https://github.com/user-attachments/assets/5df21ddf-decb-4823-ac88-92dd242cad03" />

  <img width="1049" height="449" alt="ReadDashboard 2" src="https://github.com/user-attachments/assets/f0fd9b24-e004-41f5-92d5-dffaf178504b" />

  

### Filter Transaction (income/expense)

* `POST /transactions/filter` (Filter Transaction)

  <img width="1067" height="615" alt="Filter Income" src="https://github.com/user-attachments/assets/bcd1c37a-de3d-45be-b299-b5fc550b9850" />

---


# Authentication Flow

1. User registers with email.
2. System creates activation token.
3. System sends activation link by email.
4. User activates account via link.
5. User logs in and gets JWT.
6. JWT is required for protected APIs.

---

# Scheduler and Email

This project sends two automated email notifications using scheduled jobs.

*  Daily Income & Expense Reminder 
   Method: `sendDailyIncomeExpenseReminder`  
   This email reminds the user to add daily income and expenses.  

   
	<img width="1010" height="351" alt="Daily reminder " src="https://github.com/user-attachments/assets/0851045b-85dd-4404-a859-daf35a3c917a" />


*  Daily Expense Summary  
   Method: `sendDailyExpenseSummary`    
   This email sends a daily expense summary in tabular format.  

	<img width="1014" height="389" alt="Today expenses summary" src="https://github.com/user-attachments/assets/6033ed0f-2f7e-4256-ab18-d29403f44e22" />


Mail configuration is managed using application properties or environment variables.

# PostMan Structure 


<img width="242" height="553" alt="postman structure" src="https://github.com/user-attachments/assets/a096799a-3b69-451b-ab38-8be3cf331ae2" />

---

# How to Run Locally

1. Clone the repo.
2. Create a MySQL database.
3. Copy `application.properties.example` to `application.properties`.
4. Set DB and mail values in `application.properties`.
5. Build and run the app: `mvn spring-boot:run` or run from IDE.
6. Import Postman collection to test APIs.

---


# Example application.properties 

```properties
# Configure MySQL datasource and Hibernate JPA
spring.datasource.url=jdbc:mysql://localhost:3306/walletledgerdb
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
server.servlet.context-path=/api/v1.0


#Hibernate/JPA  Setting to auto create / updates tables
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.formate_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.use_sql_comments=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql=TRACE

#Mail details
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your-email@example.com
spring.mail.password=your-mail-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.protocol=smtp
spring.mail.properties.mail.smtp.from= example@gmail.com


#Jwt token
jwt.secret=
jwt.expiration=86400000

money.manager.frontend.url=http://localhost:5173
```

Update values before running.

---

# Environment Variables

You can also use env vars for secrets.
Common keys:

* `DB_URL`
* `DB_USER`
* `DB_PASS`
* `MAIL_HOST`
* `MAIL_USER`
* `MAIL_PASS`
* `JWT_SECRET`

---

# Postman Collection

Export the Postman collection used for testing this project.  
Add the file to the repository at:

`postman/WalletLedger.postman_collection.json`

Also add a Postman environment with:
- Base API URL  
- JWT token variable  

This helps others test APIs easily.

---

# Tests

All APIs were tested using Postman.  
Unit and integration tests can be added later.

Recommended test areas:
- Authentication and JWT  
- Service layer logic  
- Scheduler and email services  

---

# Deployment Notes

- Do not commit secrets or credentials.  
- Use environment variables in production.  
- Use a managed MySQL or cloud database.  
- Configure a valid mail provider for email notifications.  

---

# Future Enhancements

- Add frontend UI (React or Angular).  
- Add monthly and yearly reports.  
- Add more export options (CSV, PDF).  
- Add role-based access and admin features.  

---

# Contributing

- Fork the repository.  
- Create a new feature branch.  
- Commit changes with clear messages.

--- 

# License

This project is licensed for personal and educational use only.  
Commercial use is not permitted without permission.

---

# Contact

Name: Ankit  
Project: WalletLedger  
Email: ankitk.software@gmail.com  
GitHub: https://github.com/Ankit-kumar8-1
