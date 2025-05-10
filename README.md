# Movies Webapp

A comprehensive movie web application built with Spring Boot. This project provides RESTful APIs and a web interface for managing movies, users, categories, reviews, and more. It includes features such as user authentication, social login linking, movie search, and multimedia support.

## Features

- Movie management: add, update, delete (soft and hard), restore movies
- User management: registration with avatar upload, password reset/change, social login linking (Google, Facebook)
- Category-based movie browsing and search with pagination and sorting
- RESTful API endpoints for movies, users, categories, reviews, and more
- Secure authentication and authorization using Spring Security and JWT
- Multimedia support for movie images and video streaming
- Admin and user roles with role-based access control
- Integration with MySQL database for persistent storage
- OpenAPI UI for API documentation and testing
- Thymeleaf-based web interface for frontend rendering

## Technologies Used

- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- Spring Security with JWT
- MySQL
- Lombok
- Thymeleaf
- Springdoc OpenAPI UI
- ModelMapper
- Maven

## Getting Started

### Prerequisites

- Java 17 JDK
- Maven 3.x
- MySQL database

### Build and Run

1. Clone the repository:

   ```bash
   git clone <repository-url>
   cd movies-webapp
   ```

2. Configure your database connection in `src/main/resources/application.properties`.

3. Build the project using Maven:

   ```bash
   mvn clean install
   ```

4. Run the application:

   ```bash
   mvn spring-boot:run
   ```

The application will start on the default port 8080.

## API Overview

The application exposes RESTful endpoints under `/api/` for various resources:

- **Movies**: `/api/movie/*`, `/api/movies/*`
- **Users**: `/api/users/*`
- **Categories**: `/api/category/*`
- **Reviews**: `/api/review/*`
- **Authentication**: `/api/auth/*`
- **Other controllers**: Ads, Payment, Watchlist, History, etc.

## Configuration

Configure your database and other settings in `src/main/resources/application.properties`. Typical settings include:

- Spring datasource URL, username, password
- JWT secret and expiration
- Mail server settings for password reset emails

## License

This project is licensed under the Apache 2.0 License.

## Contact / Contribution

Feel free to fork and submit pull requests. For issues or feature requests, please open an issue on GitHub.
