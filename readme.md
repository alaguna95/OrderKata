# Orderkata

## Overview

Orderkata is a Spring Boot application designed to load order data from a remote API and save it to a local database. 
Also it has a process to generate a csv with the processed orders.

## Prerequisites

- Java 17
- Maven
- Docker (for running the database)

## Setup

1. Clone the repository:
    ```sh
    git clone https://github.com/alaguna95/OrderKata.git
    cd orderkata
    ```

2. Build the project:
    ```sh
    mvn clean install
    ```

3. Start the database using Docker:
    ```sh
    docker-compose up -d
    ```

## Running the Application

### Tasks

#### TaskLoadOrders

**Description**: This task is responsible for loading order data from a remote API and saving it to a local database as
unprocessed orders. In case of not sent optional parameters, the task loads all the pages.

**Parameters**:
- `initPage` (optional): The initial page number to start loading data from. Must be an integer between 1 and 10000.
- `lastPage` (optional): The last page number to load data from. Must be an integer between 1 and 10000. 

```sh
mvn spring-boot:run -Dspring-boot.run.arguments="loadOrders 1 100"
```

```sh
mvn spring-boot:run -Dspring-boot.run.arguments="loadOrders"
```

#### TaskProcessOrders

**Description**: This task processes unprocessed orders stored in the local database.

```sh
mvn spring-boot:run -Dspring-boot.run.arguments="processOrders"
```

#### TaskGenerateCsv

**Description**: This task generates a csv with the processed orders. 
And logs with the resumes required.

```sh
mvn spring-boot:run -Dspring-boot.run.arguments="generateCsv"
```


