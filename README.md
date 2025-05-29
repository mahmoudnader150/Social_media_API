# Social Media API

A RESTful API for a social media platform built with Java Spring Boot and Spring Security.

## Features

1. User authentication with JWT
2. User registration and management
3. Post creation and management
4. Comment system
5. Like system
6. Follow/Unfollow functionality
7. Real-time chat functionality using WebSocket
8. User online status tracking
9. Media uploads (profile pictures, post images/videos, chat attachments)
10. Real-time notifications (new messages, posts, likes)
11. Comprehensive user profiles
12. Post saving/bookmarking
13. API versioning
14. Response filtering
15. Internationalization support

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
    "password": "string",
    "birthDate": "yyyy-MM-ddTHH:mm:ss"
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
Response: HTTP 204 No Content (empty body)

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

### Likes

#### Like a post
```http
POST /api/v1/posts/{postId}/likes
Authorization: Bearer <token>
```

#### Unlike a post
```http
DELETE /api/v1/posts/{postId}/likes
Authorization: Bearer <token>
```

#### Get like count for a post
```http
GET /api/v1/posts/{postId}/likes/count
Authorization: Bearer <token>
```

#### Check if current user liked a post
```http
GET /api/v1/posts/{postId}/likes/status
Authorization: Bearer <token>
```

### Users

#### Get all users
```http
GET /api/v1/users
Authorization: Bearer <token>
```

#### Get a specific user
```http
GET /api/v1/users/{id}
Authorization: Bearer <token>
```

#### Get a comprehensive user profile
```http
GET /api/v1/users/{id}/profile?includeFollowers=false&includeFollowing=false
Authorization: Bearer <token>
```

#### Get current user's profile
```http
GET /api/v1/users/me/profile
Authorization: Bearer <token>
```

#### Update current user's profile
```http
PUT /api/v1/users/me/profile
Authorization: Bearer <token>
Content-Type: application/json

{
    "name": "New Name",           // optional
    "birthDate": "2025-05-29T00:00:00", // optional, ISO format
    "password": "newpassword123"  // optional
}
```

Response example:
```json
{
    "id": 1,
    "name": "New Name",
    "email": "user@example.com",
    "birthDate": "2025-05-29T00:00:00",
    "online": true,
    "lastSeen": "2025-05-29T12:34:56"
}
```

#### Get user status
```http
GET /api/v1/users/{id}/status
Authorization: Bearer <token>
```

#### Delete a user
```http
DELETE /api/v1/users/{id}
Authorization: Bearer <token>
```

### Media

#### Upload media file
```http
POST /api/v1/media/upload
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <file>
type: "profile" | "post" | "chat"
```

#### Download media file
```http
GET /api/v1/media/{id}
Authorization: Bearer <token>
```

#### Delete media file
```http
DELETE /api/v1/media/{id}
Authorization: Bearer <token>
```

### Saved Posts

#### Save a post
```http
POST /api/v1/saved-posts/{postId}
Authorization: Bearer <token>
```

#### Unsave a post
```http
DELETE /api/v1/saved-posts/{postId}
Authorization: Bearer <token>
```

#### Check if a post is saved
```http
GET /api/v1/saved-posts/{postId}/status
Authorization: Bearer <token>
```

#### Get all saved posts
```http
GET /api/v1/saved-posts?page=0&size=10
Authorization: Bearer <token>
```

### Notifications

#### Get user notifications (paginated)
```http
GET /api/v1/notifications?page=0&size=20
Authorization: Bearer <token>
```

#### Get unread notifications
```http
GET /api/v1/notifications/unread
Authorization: Bearer <token>
```

#### Get unread notification count
```http
GET /api/v1/notifications/count
Authorization: Bearer <token>
```

#### Mark notification as read
```http
PATCH /api/v1/notifications/{id}/read
Authorization: Bearer <token>
```

#### Mark all notifications as read
```http
PATCH /api/v1/notifications/read-all
Authorization: Bearer <token>
```

### WebSocket Endpoints

#### Chat WebSocket
```http
ws://localhost:8080/ws
```

#### Notification WebSocket
```http
ws://localhost:8080/notification-ws
```

#### User Status WebSocket
```http
ws://localhost:8080/ws
```

## WebSocket Communication

### Chat Messages

Send message:
```javascript
stompClient.send("/app/send", {}, JSON.stringify({
    receiverId: "123",
    content: "Hello!"
}));
```

Receive messages:
```javascript
stompClient.subscribe('/user/queue/messages', function(message) {
    const messageData = JSON.parse(message.body);
    console.log(messageData);
});
```

### Notifications

Receive notifications:
```javascript
stompClient.subscribe('/user/queue/notifications', function(notification) {
    const data = JSON.parse(notification.body);
    console.log(data);
});
```

### User Status

Update status:
```javascript
stompClient.send("/app/user.status", {}, JSON.stringify(true)); // Online
stompClient.send("/app/user.status", {}, JSON.stringify(false)); // Offline
```

Receive status updates:
```javascript
stompClient.subscribe('/topic/user.status', function(statusUpdate) {
    const data = JSON.parse(statusUpdate.body);
    console.log(data);
});
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

The application uses H2 in-memory database by default. You can access the H2 console at:
```
http://localhost:8080/h2-console
```

Connection details (default):
- JDBC URL: `jdbc:h2:mem:social_media_db`
- Username: `sa`
- Password: (empty)

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License.
