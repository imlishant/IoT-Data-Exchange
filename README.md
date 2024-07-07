# IoT-Data-Exchange
Building in Java using Vert.x framework


## Project Setup
Clone Repository:

```bash

git clone https://github.com/imlishant/IoT-Data-Exchange.git
cd IoTdata
```

## Build Project:

```bash
mvn clean package
```

## Define API Endpoints
* Define CRUD endpoints for managing IoT data in MainVerticle.java. Each endpoint should handle operations like create, read, update, delete.

## Database Integration
* Choose PostgreSQL or MongoDB for database integration.
* Update database configurations in DatabaseService.java.
* Implement CRUD operations using Vert.x SQL client.

## Authentication Layer
* Implement authentication mechanisms in MainVerticle.java.
* Utilize Vert.x built-in support or integrate third-party libraries like JWT.

## Verticles and Event Bus
* Create different Verticles for distinct components of the application.
* Demonstrate communication between Verticles using Vert.x event bus.

## Future & Promise
* Use Future and Promise for handling asynchronous operations in DatabaseService.java and route handlers.

## JUnit Test Cases
* Write comprehensive JUnit test cases for API endpoints, database interactions, and authentication logic.
* Use Vert.x testing utilities (vertx-unit, vertx-junit5) for testing Verticles and handlers.


## Dockerization

* Create Dockerfile:

```dockerfile
FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

COPY target/IoTdata-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]
```

* Build Docker Image:

```bash
docker build -t iotdata-app .
```

* Run Docker Container:

```bash
docker run -p 8080:8080 iotdata-app
```
