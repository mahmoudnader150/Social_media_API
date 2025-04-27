# Social Media API

A RESTful API for a social media platform built with Java Spring Boot and Spring Security.

## Features

- User authentication with JWT
- User registration and management
- Post creation and management
- Comment system
- API versioning
- Response filtering
- Internationalization support

## Prerequisites

- Java 17 or higher
- Maven
- H2 Database (in-memory)

## Getting Started

1. Clone the repository
2. Build the project:
   ```bash
   ./mvnw clean install
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Documentation

### Authentication

#### Register a new user
```http
POST /auth/register
Content-Type: application/json

{
    "name": "string",
    "email": "string",
    "password": "string"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
    "email": "string",
    "password": "string"
}
```

Response:
```json
{
    "token": "string",
    "email": "string",
    "name": "string"
}
```

### Posts

#### Get all posts (paginated)
```http
GET /api/v1/posts?page=0&size=10
Authorization: Bearer <token>
```

#### Get a specific post
```http
GET /api/v1/posts/{id}
Authorization: Bearer <token>
```

#### Create a post
```http
POST /api/v1/posts
Authorization: Bearer <token>
Content-Type: application/json

{
    "content": "string"
}
```

#### Update a post
```http
PUT /api/v1/posts/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
    "content": "string"
}
```

#### Delete a post
```http
DELETE /api/v1/posts/{id}
Authorization: Bearer <token>
```

### Comments

#### Get comments for a post
```http
GET /api/v1/posts/{postId}/comments
Authorization: Bearer <token>
```

#### Create a comment
```http
POST /api/v1/posts/{postId}/comments
Authorization: Bearer <token>
Content-Type: application/json

{
    "content": "string"
}
```

#### Update a comment
```http
PUT /api/v1/posts/{postId}/comments/{commentId}
Authorization: Bearer <token>
Content-Type: application/json

{
    "content": "string"
}
```

#### Delete a comment
```http
DELETE /api/v1/posts/{postId}/comments/{commentId}
Authorization: Bearer <token>
```

### Users

#### Get all users
```http
GET /users
Authorization: Bearer <token>
```

#### Get a specific user
```http
GET /users/{id}
Authorization: Bearer <token>
```

#### Create a user
```http
POST /users
Authorization: Bearer <token>
Content-Type: application/json

{
    "name": "string",
    "email": "string",
    "password": "string",
    "birthDate": "yyyy-MM-dd"
}
```

#### Delete a user
```http
DELETE /users/{id}
Authorization: Bearer <token>
```

## Security

- All endpoints except `/auth/register` and `/auth/login` require authentication
- JWT tokens expire after 24 hours
- Users can only update/delete their own posts and comments

## Error Handling

The API returns appropriate HTTP status codes and error messages:

```json
{
    "timestamp": "2024-04-24T16:45:00.000Z",
    "status": 400,
    "error": "Bad Request",
    "message": "Error message here"
}
```

Common status codes:
- 200: Success
- 201: Created
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 500: Internal Server Error

## Database

The application uses H2 in-memory database. You can access the H2 console at:
```
http://localhost:8080/h2-console
```

JDBC URL: `jdbc:h2:mem:social_media_db`
Username: `sa`
Password: (empty)

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License.
