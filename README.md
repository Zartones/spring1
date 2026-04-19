# Online Book store

Simple implementation of book store application using Java Spring Boot. This project demonstrates how to design and develop a RESTful backend for managing books, users, and orders, using modern Java development practices.
## Tech Stack

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* Spring Web
* Docker
* Swagger
* MySQL
* Liquibase
* Maven
##  Entities
* User: Contains information about the registered user including their authentication details and personal information.
* Role: Represents the role of a user in the system, for example, admin or user.
* Book: Represents a book available in the store.
* Category: Represents a category that a book can belong to.
* ShoppingCart: Represents a user's shopping cart.
* CartItem: Represents an item in a user's shopping cart.
* Order: Represents an order placed by a user.
* OrderItem: Represents an item in a user's order.
![](C:/Users/antch/Desktop/Screenshot_6.png)
## Human Roles
* **Shopper** (User): Someone who looks at books, puts them in a basket (shopping cart), and buys them.
* **Manager** (Admin): Someone who arranges the books on the shelf and watches what gets bought

**Things Shoppers Can Do:**

1) Join and sign in:

* Join the store.
* Sign in to look at books and buy them.

2) Look at and search for books:

* Look at all the books.
* Look closely at one book.
* Find a book by typing its name.

3) Look at bookshelf sections:

* See all bookshelf sections.
* See all books in one section.

4) Use the basket:

* Put a book in the basket.
* Look inside the basket.
* Take a book out of the basket.

5) Buying books:

* Buy all the books in the basket.
* Look at past receipts.

6) Look at receipts:

* See all books on one receipt.
* Look closely at one book on a receipt.

**Things Managers Can Do:**

1) Arrange books:

* Add a new book to the store.
* Change details of a book.
* Remove a book from the store.

2) Organize bookshelf sections:

* Make a new bookshelf section.
* Change details of a section.
* Remove a section.

3) Look at and change receipts:

* Change the status of a receipt, like "Shipped" or "Delivered".
## Project Structure
```
src/
├── main/
│   ├── java/online/bookstore/
│   │   ├── annotation/         # Custom annotations
│   │   ├── config/             # Spring configuration classes
│   │   ├── controller/         # REST API controllers
│   │   ├── dto/                # Data Transfer Objects
│   │   │   ├── book/
│   │   │   ├── cart/
│   │   │   ├── category/
│   │   │   ├── order/
│   │   │   └── user/
│   │   ├── exception/          # Custom exceptions & global handler
│   │   ├── mapper/             # MapStruct mappers
│   │   ├── model/              # JPA entity classes
│   │   ├── repository/         # Spring Data JPA repositories
│   │   ├── security/           # JWT auth, filters, user details
│   │   ├── service/            # Business logic layer
│   │   ├── validator/          # Custom validation logic
│   │   └── BookStoreApplication.java
│   └── resources/
│       ├── db/
│       │   └── changelog/
│       │       ├── changes/    # Individual Liquibase changesets
│       │       └── db.changelog-master.yaml
│       ├── application.properties
│       └── liquibase.properties
└── test/
    └── java/online/bookstore/  # Unit & integration tests
```    
## Controllers and endpoints
<img width="1898" height="534" alt="1" src="https://github.com/user-attachments/assets/fe10aa92-5fa0-4733-96d2-611e46e5beee" />
<img width="1896" height="442" alt="2" src="https://github.com/user-attachments/assets/b51c95dd-7345-4453-8401-bf42c0900760" />
<img width="1897" height="508" alt="3" src="https://github.com/user-attachments/assets/bc4591d1-0f9d-49ae-ad3f-9a76f54ed123" />
<img width="1897" height="373" alt="4" src="https://github.com/user-attachments/assets/f06cd529-da2b-4d20-b816-1bcca2a75460" />
<img width="1900" height="235" alt="5" src="https://github.com/user-attachments/assets/74df2421-8185-48ea-aae5-0bdc04435b2e" />

## Getting Started

### Prerequisites

Make sure you have the following installed:

- [Java 17+](https://www.oracle.com/java/technologies/downloads/)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/products/docker-desktop/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Git](https://git-scm.com/)

---

### 1. Clone the repository

```bash
git clone https://github.com/Zartones/spring1.git
cd spring1
```

---

### 2. Configure environment variables

Create a `.env` file in the project root:

```env
# Database
MYSQLDB_ROOT_PASSWORD=your_root_password
MYSQLDB_DATABASE=mysql_db
MYSQLDB_USER=your_db_user
MYSQLDB_PASSWORD=your_db_password
MYSQLDB_LOCAL_PORT=3306
MYSQLDB_DOCKER_PORT=3306

# Application
SPRING_LOCAL_PORT=8088
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
```

---

### 3. Build the project

```bash
mvn clean package -DskipTests
```

---

### 4. Run with Docker Compose

```bash
docker-compose up --build
```

This will:
- Pull and start a **MySQL** container
- Wait for the database to become healthy before starting the app
- Inject datasource config into Spring Boot via `SPRING_APPLICATION_JSON`
- Run **Liquibase** migrations automatically on startup
- Start the **Spring Boot** application
- Expose a remote **debug port** (5005) for IDE debugging

To stop the containers:

```bash
docker-compose down
```

To stop and remove all volumes (wipes the database):

```bash
docker-compose down -v
```

---

### 5. Access the application

| Service    | URL                                         |
|------------|---------------------------------------------|
| REST API   | http://localhost:8088                       |
| Swagger UI | http://localhost:8088/swagger-ui/index.html |
| MySQL      | localhost:3306                              |
| Debug port | 5005                                        |

---
## Contacts
**Email:** antchayka@gmail.com


