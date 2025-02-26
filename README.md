# AWS Users Manager
## Overview
AWS Users Manager is a Spring Boot application that retrieves IAM users from an AWS account and exposes them through a REST API. 
It fetches user details such as username, user ID, MFA status, and group memberships.

## Features
- Fetches IAM users from AWS
- Returns user details in JSON format
- Handles exceptions and provides structured error responses

## Prerequisites
Ensure you have the following installed and configured:
- Java 17 or higher
- Maven 3.9.0 or higher
- AWS CLI configured with appropriate IAM credentials

## Installation & Setup
Clone the repository:
```commandline
git clone https://github.com/danijeldragicevic/aws-users-manager.git
cd aws-users-manager
```
Build the project:
```commandline
mvn clean install
```
Run the application:
```commandline
mvn spring-boot:run
```
## API Usage
### Get all IAM users
Endpoint: `GET /users` <br>
Example Request:
```commandline
curl -X GET http://localhost:8080/users
```
Example Response:
```commandline
[
  {
    "userName": "john.doe",
    "userId": "AID123456789",
    "mfaEnabled": true,
    "groups": ["admins", "developers"]
  },
  {
    "userName": "jane.smith",
    "userId": "AID987654321",
    "mfaEnabled": false,
    "groups": []
  }
]
```
## Error Handling
The application provides meaningful error responses.
Example of a 404 Not Found response:
```commandline
{
  "timestamp": "2024-02-24T12:00:00",
  "status": 404,
  "message": "Resource not found",
  "path": "/invalid-endpoint"
}
```
## Running Tests
The project includes JUnit tests for repositories, services, controllers, and exception handling.
Run tests with:
```commandline
mvn test
```
## Contributing
Contributions are welcome! Feel free to submit a pull request or open an issue.

## License
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
