# Job Portal Backend API

A Spring Boot REST API for a job portal application that manages users, companies, job postings, applications, reviews, and user profile data.

This project is designed as the backend of a job portal platform where employers can create job postings, job seekers can apply to jobs, users can leave company reviews, and profile information can be managed through RESTful APIs.

## Features

### User Management
- Register new users
- Delete users
- Retrieve all users
- Fetch a user by ID

### Company Management
- Add, update, and delete companies
- Search companies by name, location, and size
- Upload and retrieve company logos

### Job Post Management
- Create, update, and delete job posts
- Retrieve all job posts
- Filter jobs by:
  - title
  - location
  - type
  - experience level
  - salary
  - visa sponsorship
  - posting date

### Applications
- Apply for a job
- Retrieve applications
- Update application status
- Supported application statuses:
  - `PENDING`
  - `ACCEPTED`
  - `REJECTED`

### Reviews
- Add, update, and delete reviews
- Retrieve all reviews
- Retrieve reviews by company
- Sort reviews by rating

### User Profile Data
- Add, update, and delete user profile details
- Manage:
  - mobile number
  - address
  - experience
  - date of birth

---

## Getting Started

You can build and run the application locally using Maven or with Docker. A simple `docker-compose` command will build and start the service along with any dependent containers (e.g. database).

```bash
docker-compose up --build
```

You can add `-d` to the command to run containers in the background. Make sure you have a `docker-compose.yml` pointing at the `Dockerfile` (example below:

```yaml
version: "3.8"
services:
  api:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
  db:
    image: mysql:8
    environment:
      - MYSQL_ROOT_PASSWORD=secret
      - MYSQL_DATABASE=careerconnect
``` 

## Tech Stack

- **Java**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **Hibernate**
- **Lombok**
- **Maven**
- **REST APIs**
- **MySQL / PostgreSQL** depending on configuration


## Testing

The codebase includes a suite of unit tests written with **JUnit 5** and **Mockito**.  Tests cover service logic and can be executed by running:

```bash
mvn test
```

Additional tests should be added under `src/test/java` as the project grows.

---

## Project Architecture

The project follows a layered architecture for clean separation of concerns:

- **Rest**  
  Contains REST controllers that expose HTTP endpoints

- **Services**  
  Contains business logic and service implementations

- **DAO**  
  Contains repository interfaces for database operations

- **Entity**  
  Contains JPA entity classes mapped to database tables

- **DTO**  
  Contains request and response payload classes

- **Security**  
  Contains authentication and security configuration

- **Exception**  
  Contains global exception handling

- **Util**  
  Contains utility classes such as authentication helpers

---

## Project Structure

```bash
src/
├── main/
│   ├── java/
│   │   └── org/example/grad_gateway2/
│   │       ├── DAO/
│   │       │   ├── ApplicationRepository.java
│   │       │   ├── CompanyRepository.java
│   │       │   ├── JobPostRepository.java
│   │       │   ├── ReviewsRepository.java
│   │       │   ├── UserDataRepository.java
│   │       │   └── UserRepository.java
│   │       ├── DTO/
│   │       │   ├── ApplicationDTO.java
│   │       │   ├── CompanyDTO.java
│   │       │   ├── JobPostDTO.java
│   │       │   ├── JobPostResponseDTO.java
│   │       │   ├── LoginDTO.java
│   │       │   ├── RegisterDTO.java
│   │       │   ├── ReviewsDTO.java
│   │       │   ├── ReviewsReponseDTO.java
│   │       │   ├── UserDataDTO.java
│   │       │   └── UserResponseDTO.java
│   │       ├── Entity/
│   │       │   ├── Applications.java
│   │       │   ├── Company.java
│   │       │   ├── JobPost.java
│   │       │   ├── Reviews.java
│   │       │   ├── User.java
│   │       │   └── UserData.java
│   │       ├── Exception/
│   │       │   └── GlobalExceptionHandler.java
│   │       ├── Rest/
│   │       │   ├── ApplicationRestController.java
│   │       │   ├── AuthController.java
│   │       │   ├── CompanyRestController.java
│   │       │   ├── JobPostRestController.java
│   │       │   ├── ReviewRestController.java
│   │       │   └── UserRestController.java
│   │       ├── Security/
│   │       │   ├── SecurityConfig.java
│   │       │   └── UserPasswordAuthProvider.java
│   │       ├── Services/
│   │       │   ├── ApplicationService.java
│   │       │   ├── ApplicationServiceImpl.java
│   │       │   ├── CompanyService.java
│   │       │   ├── CompanyServiceImpl.java
│   │       │   ├── JobPostService.java
│   │       │   ├── JobPostServiceImpl.java
│   │       │   ├── ReviewService.java
│   │       │   ├── ReviewServiceImpl.java
│   │       │   ├── UserDataService.java
│   │       │   ├── UserDataServiceImpl.java
│   │       │   ├── UserService.java
│   │       │   └── UserServiceImpl.java
│   │       ├── Util/
│   │       │   └── AuthenticationDetails.java
│   │       └── GradGateway2Application.java
│   └── resources/
│       └── application.properties
├── test/
│   └── java/
│       └── org/example/grad_gateway2/
│           └── GradGateway2ApplicationTests.java
├── mvnw
├── mvnw.cmd
└── pom.xml
