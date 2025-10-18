# Rag Chat Storage Service

## Project Overview
The **Rag Chat Storage Service** is a Spring Boot microservice responsible for managing chat sessions and messages between users and AI. It includes features like session creation, message storage, API key-based security, and rate limiting. The service persists data in PostgreSQL and supports JSON context for messages.

---

## Technology Stack
- **Backend:** Java 17, Spring Boot 3.5.6  
- **Database:** PostgreSQL  
- **ORM:** Spring Data JPA, Hibernate  
- **DTO Mapping:** MapStruct  
- **Build Tool:** Maven  
- **Security:** API Key authentication (via Spring Security filter)  
- **Testing:** JUnit, Mockito  
- **Docker:** Dockerfile + docker-compose  

---

## Project Structure
rag-chat-storage/
├── src/main/java/com/northbay/rag_chat_storage
│ ├── controller # REST controllers
│ ├── service # Business logic and services
│ ├── models # JPA entities (ChatSession, ChatMessage)
│ ├── repository # Spring Data JPA repositories
│ ├── config # Security, API key filter
│ └── dto # DTOs for request/response mapping
├── src/main/resources
│ ├── application.properties
│ └── data.sql (optional)
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md

---

## Key Features
1. **Chat Sessions**
   - Create, update, and retrieve chat sessions.
   - Each session belongs to a user (`userId`) and has a name.
   
2. **Chat Messages**
   - Messages are linked to a session.
   - Each message contains:
     - `sender`: `"user"` or `"AI"`
     - `content`: Message text
     - `context` (optional): JSON context
     - `createdAt`: Timestamp

3. **API Key Authentication**
   - Requests are secured using **API keys**.
   - Multiple keys can be configured in `application.properties`.
   - Incoming requests must include a valid API key in headers.

4. **DTO Mapping**
   - MapStruct is used for converting between **entity** and **DTO** classes.
   - Ensures clean separation of request/response models from JPA entities.

5. **Health Check & Actuator**
   - `/actuator/health` endpoint is available to check service status.
   - Other actuator endpoints can be enabled as needed.

6. **Error Handling**
   - Standard error codes are returned for scenarios like:
     - `404` – Session or message not found
     - `403` – Invalid API key
     - `400` – Missing required parameters
   - Global exception handling via `@ControllerAdvice`.

7. **Rate Limiting**
   - Optional: Can implement rate limiting per API key or per user (using Redis or in-memory for demo).

---

## Database Schema

### ChatSession
| Column     | Type          | Notes                    |
|------------|---------------|--------------------------|
| id         | UUID          | Primary Key              |
| userId     | VARCHAR       | ID of user               |
| name       | VARCHAR       | Name of session          |
| favorite   | BOOLEAN       | Default false            |
| createdAt  | TIMESTAMP     | Default now()            |
| updatedAt  | TIMESTAMP     | Default now()            |

### ChatMessage
| Column     | Type          | Notes                    |
|------------|---------------|--------------------------|
| id         | UUID          | Primary Key              |
| session_id | UUID          | Foreign key to ChatSession |
| sender     | VARCHAR       | `"user"` or `"AI"`       |
| content    | TEXT          | Message text             |
| context    | JSONB         | Optional JSON context    |
| createdAt  | TIMESTAMP     | Default now()            |

---

## Endpoints

### Chat Sessions
| Method | Endpoint                  | Description                  | Params         |
|--------|---------------------------|------------------------------|----------------|
| GET    | `/api/v1/sessions`        | Get sessions for user        | `userId` (query) |
| POST   | `/api/v1/sessions`        | Create new session           | `userId`, `name` (body) |
| GET    | `/api/v1/sessions/{id}`   | Get session by ID            | `{id}` (path) |

### Chat Messages
| Method | Endpoint                                        | Description                  | Params         |
|--------|------------------------------------------------|------------------------------|----------------|
| POST   | `/api/v1/sessions/{sessionId}/messages`       | Add new message              | `sender`, `content`, `context` (body) |
| GET    | `/api/v1/sessions/{sessionId}/messages`       | Get messages for session     | `{sessionId}` (path) |

### Health Check
| Method | Endpoint          | Description       |
|--------|-----------------|------------------|
| GET    | `/actuator/health` | Check service health |

---

## Setup and Running

1. **Clone the repository**
```bash
git clone <repo-url>
cd rag-chat-storage
Build using Maven
mvn clean install -U
Run the service
mvn spring-boot:run
Run with Docker
docker build -t rag-chat-storage .
docker run -p 8081:8081 rag-chat-storage
Database configuration
Set your PostgreSQL credentials in .env or application.properties:
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/rag_chat_storage
SPRING_DATASOURCE_USERNAME=rag_user
SPRING_DATASOURCE_PASSWORD=secret
Testing
Unit tests using JUnit 5 and Mockito are in src/test/java.
To run tests:
mvn test
Notes / Recommendations
Multiple API keys can be configured in application.properties.
MapStruct automatically maps DTOs → Entities.
Standard error handling and logging implemented for all endpoints.
Use Postman or curl for testing:
curl -X POST http://localhost:8081/api/v1/sessions/<sessionId>/messages \
-H "x-api-key: key1" \
-d '{"sender":"user","content":"Hello","context":{"topic":"support"}}'