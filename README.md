# eCommerce Backend Application (Spring Boot)

This repository contains the backend API for an eCommerce application built using Spring Boot.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

This application provides the backend services for an eCommerce platform, enabling users to browse products, manage their shopping carts, place orders, and manage their accounts. It's designed for scalability and maintainability, leveraging Spring Boot's capabilities for rapid development and robust API creation.

## Features

-   User Registration and Login
-   Authentication using JWT Token
-   Role Based Authorization
-   User Management Service
-   Approval Management Service (for approvals like Seller, Product, Variant and Inventory)
-   Product Onboarding Service (Seller interface to onboard a product or variant with approvals)
-   Product Search Service
-   Cart Management Service
-   Order Management Service
-   Payment Management Service
-   Notification Service
-   Tracking Management Service

## Technologies Used

-   Spring Boot (Java)
-   Spring Data JPA (for database interaction)
-   Spring Security (for authentication and authorization)
-   MySQL Database
-   MongoDB
-   Maven
-   Lombok (for reducing boilerplate code)
-   JWT (JSON Web Tokens)
-   Apache Kafka
-   Redis
-   ElasticSearch

## Prerequisites

List the software and tools that need to be installed before running the application.

**Example:**

-   Java Development Kit (JDK) 17 or higher
-   Maven
-   MySQL Database
-   An IDE (e.g., IntelliJ IDEA, Eclipse, VS Code)

## Installation

1.  Clone the repository:

    ```bash
    git clone <repository_url>
    ```

2.  Navigate to the project directory:

    ```bash
    cd <project_directory>
    ```

3.  Navigate to Services directory:

    ```bash
    cd services
    ```

4. Update docker-compose.yml with required data in the placeholders like passwords for database etc.

5. Install required software mentioned in the docker-compose.yml such as MySQL, MongoDB, Kafka, ElasticSearch, Redis etc.
    ```bash
   docker-compose up -d
    ```

## Configuration

1.  Configuration Service is designed to hold the configurations required for all the services.
2.  Go through the configurations of each microservice and update the data like passwords in the respective placeholders.
3.  Starter data also mentioned under the starters directory of each service. Go through the InitializeData and update data such as IDs in the starters files.

## Running the Application

1.  Navigate to each microservice.
2.  Run the application using Maven:

    ```bash
    mvn spring-boot:run
    ```
3.  Microservices will start on the respective ports mentioned as per the configuration.

## API Documentation
-   The API documentation can be accessed at `http://localhost:<port_no_of_the_service>/swagger-ui.html`

## Contributing
1.  Fork the repository.
2.  Create a new branch for your feature or bug fix:

    ```bash
    git checkout -b feature/your-feature-name
    ```

3.  Make your changes and commit them:

    ```bash
    git commit -m "Add your feature or fix"
    ```

4.  Push your changes to your fork:

    ```bash
    git push origin feature/your-feature-name
    ```

5.  Create a pull request to the `main` branch of the original repository.

## License
This project is licensed under the Apache License. See the [LICENSE](LICENSE) file for details.
