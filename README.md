# TESTING GUIDE

This guide shows how to run the application, set up WireMock with Docker Compose, and test the API with sample curl commands.

---

## 1. Running the Application

**Build and start the Spring Boot app:**
```sh
  ./gradlew bootRun
```
By default, the app runs on port `8080`.

---

## 2. Running WireMock with Docker Compose

use the `docker-compose.test.yml` in your project root:

```yaml
version: '3.8'
services:
  crazyair-wiremock:
    image: wiremock/wiremock:3.6.0
    ports:
      - "8081:8080"
    volumes:
      - ./wiremock/crazyair:/home/wiremock/mappings
  toughjet-wiremock:
    image: wiremock/wiremock:3.6.0
    ports:
      - "8082:8080"
    volumes:
      - ./wiremock/toughjet:/home/wiremock/mappings
```

- Place your WireMock stub mappings in `./wiremock/crazyair` and `./wiremock/toughjet`.
- Each subdirectory should contain JSON files defining the stubbed endpoints for each supplier.

**Start WireMock containers:**
```sh
  docker-compose -f docker-compose.test.yml up
```

---

## 3. Example WireMock Mapping (Stub)

Example: `wiremock/crazyair/crazyair-mapping.json`
```json
{
  "request": {
    "method": "POST",
    "url": "/crazyair/flights"
  },
  "response": {
    "status": 200,
    "jsonBody": [
      {
        "airline": "CrazyAir",
        "price": 123.45,
        "cabinclass": "E",
        "departureAirportCode": "LHR",
        "destinationAirportCode": "AMS",
        "departureDate": "2025-05-01T10:00:00Z",
        "arrivalDate": "2025-05-01T12:00:00Z"
      }
    ]
  }
}
```

---

## 4. Sample cURL Command for Testing

```sh
  curl -X GET "http://localhost:8080/flights/search?origin=LHR&destination=AMS&departureDate=2025-05-01&returnDate=2025-05-02&numberOfPassengers=1" -H "accept: application/json"
```

---

## 5. Notes
- Ensure your `application.yml` points the supplier URLs to `http://localhost:8081/crazyair` and `http://localhost:8082/toughjet`.
- You can add or modify WireMock mappings to simulate different supplier responses.
- Stop the containers with:
```sh
  docker-compose -f docker-compose.test.yml down
```

## 6. Running All Tests

Run all unit and integration tests:
```sh
  ./gradlew test
```

---
