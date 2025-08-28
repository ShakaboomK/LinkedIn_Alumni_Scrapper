# Alumni Profile Searcher - Spring Boot Backend

## Overview

This project is a backend Spring Boot application designed to search and store alumni profiles from LinkedIn based on specific criteria such as university, job designation, and optional pass-out year. It integrates with **PhantomBuster's LinkedIn API** to scrape alumni data and saves the retrieved profiles into a PostgreSQL database.

The backend exposes RESTful APIs allowing clients to trigger searches and receive stored alumni profile data.

---

## Features

- **Search Alumni Profiles API:** Receives university name, designation, and optional passout year; triggers PhantomBuster agent to scrape LinkedIn.
- **Webhook Endpoint:** Receives results asynchronously from PhantomBuster, parses the alumni data, and saves profiles reliably to the database.
- **Database Integration:** Uses PostgreSQL to persist alumni profile details.
- **Duplicate Handling:** Prevents storing duplicate profiles by checking LinkedIn URLs.
- **Robust Error Handling:** Validates input data and handles empty or invalid payloads gracefully.
- **RESTful & Testable:** Backend APIs are REST-compliant, easily testable via Postman or similar tools. No UI is included as per requirements.
- **Unit & Integration Testing:** Includes tests for service logic and API endpoints, ensuring quality and correctness.

---

## Technology Stack

- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- Jackson for JSON parsing
- Lombok for boilerplate reduction
- JUnit 5 & Mockito for testing
- Reactor (Spring WebFlux) for reactive API clients (PhantomBuster)
- Maven for build and dependency management
- ngrok for local webhook url connection (required for PhantomBuster API)

---

## API Endpoints

### 1. **POST** `/api/alumni/search`

- **Description:** Trigger a PhantomBuster Linkedin search with university, designation, and optional passout year.
- **Request Body (JSON):**

{
"university": "Name of University",
"designation": "Job Designation",
"passoutYear": 2020 (optional)
}

text

- **Response:** JSON including status and launched agent details (typically reactive with status).

---

### 2. **POST** `/api/alumni/webhook`

- **Description:** PhantomBuster webhook endpoint to receive scraped alumni data asynchronously.
- **Payload:** Contains an envelope with metadata and a key `resultObject` holding JSON string or array of alumni profiles.
- **Processing:** Parses each profile, validates required fields (e.g., LinkedIn URL), and stores in the database.
- **Response:** 200 OK on success or appropriate error code otherwise.

---

### 3. **GET** `/api/alumni/all`

- **Description:** Fetch all stored alumni profiles.
- **Response Example:**

{
"status": "success",
"data": [
{
"name": "John Doe",
"currentRole": "Software Engineer",
"university": "University XYZ",
"location": "City, Country",
"linkedinHeadline": "...",
"passoutYear": 2020
},
...
]
}

text

---

## Data Model

### AlumniProfile Entity Fields

| Field             | Type    | Description                           |
|-------------------|---------|-----------------------------------|
| id                | Long    | Auto-generated primary key          |
| name              | String  | Full name of the alumnus            |
| currentRole       | String  | Current job title / designation     |
| university        | String  | Educational institution name        |
| location          | String  | Location of the alumnus             |
| linkedinHeadline  | String  | LinkedIn profile headline           |
| linkedinProfileUrl| String  | LinkedIn profile URL (unique & not null) |
| passoutYear       | Integer | Year of graduation (optional)       |

---

## Design Highlights

- **Safety Checks:** Before insertion, data is validated; entries without LinkedIn profile URLs are skipped to avoid DB errors.
- **Flexible JSON Parsing:** Supports both stringified JSON arrays and parsed JSON objects in webhook payloads.
- **Logging:** Detailed logs help trace requests, parsing, and data persistence.
- **Transactional Service Layer:** Ensures data integrity during save operations.
- **Duplicate Avoidance:** Check existing entries by unique LinkedIn URL before inserting new records.

---

## Setup Instructions

1. **Database:**
    - Set up a PostgreSQL database.
    - Create a database for the application.
    - Update `application.properties` or `application.yml` with correct DB URL, username, password.

2. **Build & Run:**
    - Use Maven to build the project:
      ```
      mvn clean install
      ```
    - Run the Spring Boot app:
      ```
      mvn spring-boot:run
      ```
    - Alternatively, run the generated jar:
      ```
      java -jar target/your-app.jar
      ```

3. **Expose Webhook Endpoint:**
    - Use tools like `ngrok` for local development webhook reception.
    - Configure PhantomBuster webhook URL to point to `/api/alumni/webhook`.

4. **Test the APIs:**
    - Use Postman or similar tools.
    - Trigger searches via POST `/api/alumni/search`.
    - Confirm results saving via webhook and fetch saved profiles via GET `/api/alumni/all`.

---

## Testing

- **Unit Tests:** Located in the `src/test/java` path, covering service methods and validation.
- **Integration Tests:** Cover controller endpoints, webhook handling, and database interactions.
- Use `mvn test` to run all tests.
- Optional: Use TestContainers for launching a real PostgreSQL container during tests.

---

## Notes & Recommendations


- Monitor skipped profiles (due to missing LinkedIn URLs) to assess data completeness.
- Ensure PhantomBuster agents are configured in your Phantom Buster Account to run successfully and the webhook URL is reachable. install ngrok locally and map your application port with ngrok using command in windows or Linux based OS
```bash

ngrok http <your port_number>
#this will create a https url which you can use for setting up the
#webhook url for your phantom buster Agent. make sure to copy the produced link
#and paste it in your Phantom Buster LinkedIn search Export Agent's webhook configuration.
```
- this will create a https 
- For production, secure APIs with authentication & HTTPS.

---

## References

- [PhantomBuster API Docs](https://phantombuster.com/api-docs)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Jackson JSON Processor](https://github.com/FasterXML/jackson)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

---

## Contact

For questions or issues, please reach out to:

- Developer:Kalva Karthik
- Email: mrkalvakarthik007@gmail.com
- GitHub: https://github.com/ShakaboomK/

---

