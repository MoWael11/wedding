# Wedding App - Backend

Spring Boot REST API for the Wedding photo sharing application.

## Tech Stack

- **Java 17**
- **Spring Boot 4.0.1**
- **Hibernate 7.2.0** (JPA)
- **MySQL 8.0** (Docker)
- **Custom Token Authentication**

## Prerequisites

- Java 17+
- Maven
- Docker & Docker Compose

## Getting Started

### 1. Copy .env.example in .env and  Start the Database

```bash
docker compose up -d
```

This starts a MySQL 8.0 container with:
- Database: `wedding_db`
- User: `wedding_user`
- Port: `3306`

### 2. Configure Environment

Create a `.env` file in the backend folder:

```env
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DATABASE=wedding_db
MYSQL_USER=wedding_user
MYSQL_PASSWORD=wedding_secret
MYSQL_ROOT_PASSWORD=root_secret

APP_BASE_URL=http://localhost:8080
FRONTEND_URL=http://localhost:4200
```

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/sign-up` | Register a new user |
| POST | `/api/auth/sign-in` | Login and get token |
| POST | `/api/auth/sign-out` | Logout (invalidate token) |

### Weddings

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/weddings` | Get all weddings |
| GET | `/api/weddings/{id}` | Get wedding by ID |
| GET | `/api/weddings/code/{code}` | Get wedding by code |
| POST | `/api/weddings` | Create a new wedding |
| PUT | `/api/weddings/{id}` | Update a wedding |
| DELETE | `/api/weddings/{id}` | Delete a wedding |

### Images

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/weddings/{id}/images` | Get images for a wedding |
| POST | `/api/weddings/{id}/images` | Upload an image |

## Authentication

All `/api/weddings/**` endpoints require authentication via Bearer token:

```
Authorization: Bearer <token>
```

Tokens are returned from the sign-in/sign-up endpoints.

## Image Upload Constraints

- **Max file size**: 5MB per image
- **Max total storage**: 200MB per wedding
- **Allowed formats**: JPG, PNG

## Project Structure

```
src/main/java/it/eforhum/backend/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/             # Request/Response DTOs
├── entity/          # JPA entities
├── exception/       # Custom exceptions
├── filter/          # Auth & CORS filters
├── repository/      # JPA repositories
└── service/         # Business logic
```

## Database Schema

- **users** - User accounts
- **tokens** - Authentication tokens
- **wedding_events** - Wedding information
- **persons** - Bride & groom details
- **images** - Uploaded photos

## Troubleshooting

### Reset Database

```bash
docker compose down -v
docker compose up -d
```