# Auth Service

## Base URL for Local Development

```
http://localhost:8081/
```

## Base URL for Production Environment

```
TBD
```

# User API v1

## Base URL

```
/user/v1
```

---

## 1. Test User Service

**GET** `/test`

### Description

Simple endpoint to check if the User service is running properly.

### Success Response – 200

```json
"User service running successfully"
```

---

## 2. Get User by ID

**GET** `/get/{userId}`

### Description

Fetches user details using the user's ID.

### Path Parameters

| Parameter | Type   | Description        |
| --------- | ------ | ------------------ |
| userId    | string | The ID of the user |

### Success Response – 200

```json
{
  "id": "user-1",
  "name": "John Doe",
  "email": "demodemo@gmail.com",
  "locationResponse": {
    "id": 4,
    "locationType": "USER",
    "address": "new address",
    "thana": "kg",
    "po": "kg",
    "city": "Sylhet",
    "postalCode": 1009,
    "zoneId": 9
  }
}
```

### Notes on Response

- location may be null (user did not provide)
- address, thana and po in location may be null

### Error Response – 404

```json
{
  "success": false,
  "error": {
    "code": "USER_NOT_FOUND",
    "message": "User with the specified ID was not found."
  }
}
```

---

## 3. Login

**POST** `/login`

### Description

Logs a user into the system.

### Request Headers

```json
{
  "Content-Type": "application/json",
  "Authorization": "Bearer <token>"
}
```

### Request Body

```json
{
  "userId": "user-1"
}
```

### Success Response – 200

```json
{
  "id": "user-1",
  "name": "John Doe",
  "email": "demodemo@gmail.com",
  "locationResponse": {
    "id": 4,
    "locationType": "USER",
    "address": "new address",
    "thana": "kg",
    "po": "kg",
    "city": "Sylhet",
    "postalCode": 1009,
    "zoneId": 9
  }
}
```

### Notes on Response

- location may be null (user did not provide)
- address, thana and po in location may be null

### Error Response – 401

```json
{
  "success": false,
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "Email or password is incorrect."
  }
}
```

---

## 4. Register

**POST** `/register`

### Description

Registers a new user.

### Request Headers

```json
{
  "Content-Type": "application/json",
  "Authorization": "Bearer <token>"
}
```

### Request Body

```json
{
  "userId": "user-1",
  "name": "John Doe",
  "email": "demodemo@gmail.com",
  "password": "john@123",
  "location": {
    "id": 4,
    "locationType": "USER",
    "address": "new address",
    "thana": "kg",
    "po": "kg",
    "city": "Sylhet",
    "postalCode": 1009,
    "zoneId": 9
  }
}
```

### Success Response – 201

```json
{
  "id": "user-1",
  "name": "John Doe",
  "email": "demodemo@gmail.com",
  "locationResponse": {
    "id": 4,
    "locationType": "USER",
    "address": "new address",
    "thana": "kg",
    "po": "kg",
    "city": "Sylhet",
    "postalCode": 1009,
    "zoneId": 9
  }
}
```

### Notes on Response

- location may be null (user did not provide)
- address, thana and po in location may be null

### Error Response – 409

```json
{
  "success": false,
  "error": {
    "code": "EMAIL_ALREADY_EXISTS",
    "message": "An account with this email already exists."
  }
}
```

---
