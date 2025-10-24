# Task Manager API

A robust REST API for task management built with Spring Boot. This application provides user authentication and task management functionality with proper error handling and security measures.

## Features

### Authentication
- User registration with email verification
- JWT-based authentication
- Secure password handling with BCrypt
- Logout functionality

### Task Management
- Create, read, update, and delete tasks
- Task status management (OPEN, IN_PROGRESS, DONE)
- User-specific task access control
- Proper error handling and validation

## Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Security with JWT
- Spring Data JPA
- H2 Database
- Maven
- Lombok
- Validation
- Error Handling with proper status codes

## Getting Started

### Prerequisites
- JDK 17 or later
- Maven 3.x

### Environment Variables
Create a `.env` file in the root directory with the following variables:
```properties
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000  # 24 hours in milliseconds
```

### Installation and Running

1. Clone the repository
```bash
git clone https://github.com/Abdelrahmansherif207/Task-Manager-API-Spring-Boot.git
cd Task-Manager-API-Spring-Boot
```

2. Build the project
```bash
mvn clean install
```

3. Run the application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

### Authentication Endpoints

#### Register User
```http
POST /auth/signup
Content-Type: application/json

{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "StrongPassword123!"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
    "email": "john@example.com",
    "password": "StrongPassword123!"
}
```

#### Verify Email
```http
POST /auth/verify
Content-Type: application/json

{
    "email": "john@example.com",
    "verificationCode": "123456"
}
```

#### Logout
```http
POST /auth/logout
Authorization: Bearer {token}
```

### Task Management Endpoints

#### Create Task
```http
POST /tasks
Authorization: Bearer {token}
Content-Type: application/json

{
    "title": "Complete project documentation",
    "description": "Write detailed documentation for the API",
    "status": "OPEN"
}
```

#### Get All Tasks
```http
GET /tasks
Authorization: Bearer {token}
```

#### Get Task by ID
```http
GET /tasks/{id}
Authorization: Bearer {token}
```

#### Update Task
```http
PUT /tasks/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
    "title": "Updated title",
    "description": "Updated description",
    "status": "IN_PROGRESS"
}
```

#### Delete Task
```http
DELETE /tasks/{id}
Authorization: Bearer {token}
```

## Error Handling

The API uses proper HTTP status codes and returns detailed error messages:

- 200: Success
- 201: Resource created
- 400: Bad request (validation errors)
- 401: Unauthorized (invalid/missing token)
- 403: Forbidden (unauthorized access)
- 404: Resource not found
- 500: Internal server error

Example error response:
```json
{
    "timestamp": "2025-10-24T10:30:45.123",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid input",
    "path": "/api/tasks"
}
```

## Postman Collection

For testing the API endpoints, you can import this Postman collection:
[Task Manager API Collection](https://.postman.co/workspace/My-Workspace~941cd4ae-3b33-47d8-8be4-e377ae7b6c68/collection/33329821-f9bd0e46-9ac2-4a32-b23b-a373355674d1?action=share&creator=33329821)

## Security Measures

- JWT-based authentication
- Password encryption using BCrypt
- User-specific task access control
- Input validation
- CORS configuration
- Protected endpoints
- Email verification

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**Abdelrahman Sherif**
- GitHub: [@Abdelrahmansherif207](https://github.com/Abdelrahmansherif207)
