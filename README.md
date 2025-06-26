# tms-iot

### **TMS-IOT – Temperature Monitoring System using IoT Architecture**

The **TMS-IOT** system is designed to monitor temperature data using an end-to-end IoT pipeline. Here's how the system works:

<p align="center">
  <img src="Assets/app_diagram.jpg" width="500" />
</p>

1. **Arduino with Temperature Sensor**
   A temperature sensor connected to an Arduino collects real-time environmental data. The Arduino transmits this data via a **serial connection** to a local application.

2. **Console Application (.NET C#)**
   A C# console application reads the serial data from the Arduino. It processes and forwards the readings to an **MQTT Broker** using a public topic.

3. **MQTT Broker**
   The MQTT Broker acts as a lightweight messaging intermediary. It publishes the temperature data, making it available to any subscribed services.

4. **Spring Application (Java)**
   A Spring Boot application subscribes to the MQTT topic, receives the temperature data, and processes it in real-time. Only user registration information is persistently stored in a PostgreSQL database. The application also exposes RESTful endpoints for accessing both user and sensor-related data.

5. **PostgreSQL Database**
   This relational database stores only user registration information, such as names, emails, and credentials. It is used for authentication, authorization, and user profile management.

6. **Web Dashboard (Angular)**
   The Angular frontend consumes data from the Spring Application via HTTP requests. It displays the temperature information in a user-friendly interface with real-time updates and visual charts.
## TMS Data Service - REST API Documentation
### REST API Overview

- **`POST /users`**: Register a new user (public)
- **`POST /auth`**: Obtain JWT using Basic Auth (email & password)
- **`GET /users/me`**: Get current user info (requires JWT)
- **`PUT /users/{id}` / `DELETE /users/{id}`**: Update/delete user (JWT required)
- **`GET /sensor`**: Retrieve the latest temperature/humidity data

Include the JWT in requests to protected endpoints:
```
Authorization: Bearer <token>
```

### Swagger UI

Interactive API documentation is available at:
```
http://localhost:9090/swagger-ui/index.html
```

## Build and Run

To build and run the full stack using Docker Compose:

**Build:**
```
docker compose build
```

**Run:**
```
docker compose up -d
```
This will start all required services in detached mode.
