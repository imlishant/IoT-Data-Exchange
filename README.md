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
### Overview:
* Define CRUD endpoints for managing IoT data in MainVerticle.java. Each endpoint should handle operations like create, read, update, delete.

### Steps:
* Create a MainVerticle.java file in the com.vertex.crud.IoTdata package.
* Implement the necessary CRUD operations as handlers in the MainVerticle.
* Example of CRUD endpoint setup in MainVerticle.java:

```java
// In MainVerticle.java
router.post("/api/create").handler(this::createData);
router.get("/api/read/:id").handler(this::getDataById);
router.get("/api/readAll").handler(this::getAllData);
router.put("/api/update/:id").handler(this::updateData);
router.delete("/api/delete/:id").handler(this::deleteData);
router.delete("/api/deleteAll").handler(this::deleteAllData);
```

## Database Integration
### Overview:
* Choose PostgreSQL for database integration.
* Update database configurations in DatabaseService.java.
* Implement CRUD operations using Vert.x SQL client.

### Steps:
* Ensure PostgreSQL is installed and running on your local machine or accessible via network.
* Create a database named testDB and a table named iot_data.
* Create a DatabaseService.java file in the com.vertex.crud.IoTdata package.
* Implement the necessary CRUD operations using Vert.x SQL client.

Example of database configuration in DatabaseService.java:

```java
// In DatabaseService.java
SqlConnectOptions connectOptions = new SqlConnectOptions()
    .setHost("localhost")
    .setPort(5432)
    .setDatabase("testDB")
    .setUser("postgres")
    .setPassword("yourpassword")
    .setSslMode(io.vertx.pgclient.SslMode.DISABLE);
```

## Authentication Layer
### Overview:
* Implement authentication mechanisms in MainVerticle.java. Utilize Vert.x built-in support or integrate third-party libraries like JWT.

### Steps:
* Create an AuthService.java file to handle authentication logic.
* Update MainVerticle.java to include authentication.

Example of authentication setup in MainVerticle.java:

```java
// In MainVerticle.java
authService = new AuthService(vertx);
JWTAuth jwtAuth = authService.getJwtAuth();
router.route("/api/*").handler(JWTAuthHandler.create(jwtAuth));
```

## Verticles and Event Bus
### Overview:
* Create different Verticles for distinct components of the application. Demonstrate communication between Verticles using Vert.x event bus.

### Steps:
* Define separate Verticles for different functionalities.
* Use the Vert.x event bus for inter-Verticle communication.

## Future & Promise
* Use Future and Promise for handling asynchronous operations in DatabaseService.java and route handlers.

## JUnit Test Cases
### Overview:
* Write comprehensive JUnit test cases for API endpoints, database interactions, and authentication logic. Use Vert.x testing utilities (vertx-unit, vertx-junit5) for testing Verticles and handlers.

### Example:
* Add dependencies for vertx-unit and vertx-junit5 in your pom.xml.
* Write test cases in a test class.


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

## Testing with Postman
1. Login to obtain JWT Token:

* Endpoint: POST /login
* Body:
```json
{
    "username": "yourUsername",
    "password": "yourPassword"
}
```

2. Using JWT Token for CRUD operations:

* Include the JWT token in the Authorization header for all API requests:
  
```text
Authorization: Bearer <your-jwt-token>
```

### Example Requests:

1. Create Data:

* Endpoint: POST /api/create
* Body:
```json
{
    "id": 1,
    "value": "someValue"
}
```

2. Get Data by ID:

* Endpoint: GET /api/read/1

3. Get All Data:

* Endpoint: GET /api/readAll

4. Update Data:

* Endpoint: PUT /api/update/1
* Body:
```json
{
    "value": "newValue"
}
```

5. Delete Data:

* Endpoint: DELETE /api/delete/1

6. Delete All Data:

* Endpoint: DELETE /api/deleteAll
