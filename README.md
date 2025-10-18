# Rag Chat Storage Service

A Spring Boot microservice for storing and managing chat sessions and messages.  
It supports secure REST APIs, API key authentication, Swagger/OpenAPI documentation, and health monitoring using Spring Boot Actuator.

---

## 🚀 Features

- CRUD APIs for Chat Sessions and Messages  
- API Key–based authentication  
- PostgreSQL/MongoDB integration (configurable)  
- Lombok for concise entities and logging  
- OpenAPI 3.0 + Swagger UI documentation  
- Spring Boot Actuator for health checks  
- Dockerized for deployment  

---

## ⚙️ Tech Stack

| Layer        | Technology                       |
|------------- |---------------------------------|
| Language     | Java 17                          |
| Framework    | Spring Boot 3.5.x               |
| Database     | PostgreSQL                       |
| ORM          | Spring Data JPA                  |
| Build Tool   | Maven                            |
| Documentation| OpenAPI / Swagger                |
| Logging      | SLF4J + Logback                  |
| Security     | Custom API Key Filter            |

---

## 🏗️ Project Structure

rag-chat-storage/
├── src/
│ ├── main/
│ │ ├── java/com/northbay/rag_chat_storage/
│ │ │ ├── controller/ # REST controllers
│ │ │ ├── service/ # Business logic
│ │ │ ├── repository/ # Spring Data JPA Repositories
│ │ │ ├── models/ # Entities (ChatSession, ChatMessage)
│ │ │ ├── config/ # Security + Swagger/OpenAPI config
│ │ │ └── RagChatStorageApplication.java
│ │ └── resources/
│ │ ├── application.yml
│ │ └── logback-spring.xml
│ └── test/
│ └── java/... # Unit and service layer tests
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md

---

## 🧰 Setup & Running Instructions

### ✅ Prerequisites
- Java 17+  
- Maven 3.9+  
- Docker (optional)  
- PostgreSQL running locally (if using SQL DB)  

### 🛠️ Build & Run Locally
```bash
# 1. Clone the repo
git clone https://github.com/meghanagaraja-git/rag-chat-storage.git
cd rag-chat-storage

# 2. Build with Maven
mvn clean install

# 3. Run the application
mvn spring-boot:run
Application runs at → http://localhost:8081
🐳 Run with Docker
docker build -t rag-chat-storage .
docker run -p 8082:8081 rag-chat-storage
Or with docker-compose:
docker-compose up --build
🔐 Authentication
All /api/v1/** endpoints require an API key.
Add this header in each request:

x-api-key: key1
You can change the key inside application.yml.
Swagger UI also supports an “Authorize” button to enter the key interactively.
📚 API Endpoints
Chat Sessions
Method	Endpoint	Description
GET	/api/v1/sessions	Get all chat sessions
GET	/api/v1/sessions/{id}	Get session by ID
POST	/api/v1/sessions	Create a new chat session
PUT	/api/v1/sessions/{id}/rename	Rename an existing session
PUT	/api/v1/sessions/{id}/favorite	Mark/unmark favorite
DELETE	/api/v1/sessions/{id}	Delete a chat session
Chat Messages
Method	Endpoint	Description
GET	/api/v1/sessions/{sessionId}/messages	Get all messages for a session
POST	/api/v1/sessions/{sessionId}/messages	Add a message to a session
DELETE	/api/v1/sessions/{sessionId}/messages/{id}	Delete a specific message
❤️ Health Check
Spring Boot Actuator endpoints:
Endpoint	Description
/actuator/health	Health status
/actuator/info	App info
/actuator/metrics	Metrics overview
Example:
curl http://localhost:8081/actuator/health
Response:
{
  "status": "UP"
}
📘 OpenAPI / Swagger Documentation
After the app starts:
Swagger UI → http://localhost:8081/swagger-ui.html
OpenAPI JSON → http://localhost:8081/v3/api-docs
Ensure your SecurityConfig permits these URLs:
.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
🧩 Example API Call
curl -X POST http://localhost:8081/api/v1/sessions \
     -H "Content-Type: application/json" \
     -H "x-api-key: key1" \
     -d '{
           "name": "Customer Support Chat",
           "userId": "user123"
         }'
🧪 Run Tests
mvn test
Unit and integration tests are in src/test/java/....
🧾 License
This project is property of NorthBay Solutions.
Internal and educational use only.