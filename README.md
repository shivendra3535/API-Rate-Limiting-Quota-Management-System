**API Rate Limiting & Quota Management System**

*Spring Boot · Redis · MySQL*

A production-ready REST API gateway with per-user rate limiting, quota enforcement, and admin analytics

**📋 Project Overview**

This system provides a complete API gateway solution that authenticates requests via API keys, enforces per-user daily quotas, applies soft-limit throttling, logs all traffic asynchronously, and exposes admin analytics endpoints.

**Tech Stack**

|                    |                             |             |
|--------------------|-----------------------------|-------------|
| **Layer**          | **Technology**              | **Version** |
| Framework          | Spring Boot                 | 4.0.1       |
| Language           | Java                        | 21          |
| Cache / Rate Limit | Redis                       | Latest      |
| Database           | MySQL                       | 8.x         |
| ORM                | Spring Data JPA / Hibernate | Auto        |
| Async              | Spring @Async               | Built-in    |
| Build Tool         | Maven                       | 3.x+        |
| Utilities          | Lombok                      | Latest      |

**🔧 Prerequisites**

Ensure the following are installed on your machine before deploying:

|                |                         |
|----------------|-------------------------|
| **Java 21**    | java -version           |
| **Maven 3.x+** | mvn -version            |
| **MySQL 8.x**  | mysql \--version        |
| **Redis**      | redis-server \--version |
| **Git**        | git \--version          |

**🚀 Step-by-Step Deployment Guide**

**Step 1 --- Clone the Repository**

> git clone https://github.com/your-username/api-rate-limiting-system.git
>
> cd api-rate-limiting-system

**Step 2 --- Start MySQL & Create Database**

Start your MySQL server, then log in and create the schema:

> mysql -u root -p
>
> CREATE DATABASE api_rate_limiting_db;
>
> EXIT;

*MySQL is configured to auto-create the database via createDatabaseIfNotExist=true, but it\'s good practice to create it manually.*

**Step 3 --- Seed the Database**

Insert the subscription plans and test users into MySQL before starting the app:

> INSERT INTO plans (name, daily_quota, soft_limit_threshold) VALUES
>
> (\'FREE\', 100, 80),
>
> (\'PRO\', 1000, 800);
>
> INSERT INTO users (api_key, plan_id) VALUES
>
> (\'free-user-key-001\', 1),
>
> (\'pro-user-key-001\', 2);

**Step 4 --- Start Redis**

Redis must be running on localhost:6379 (default). Start it with:

> redis-server

Verify it\'s running:

> redis-cli ping \# Should return: PONG

**Step 5 --- Configure application.properties**

Open src/main/resources/application.properties and update your credentials:

> spring.datasource.url=jdbc:mysql://localhost:3306/api_rate_limiting_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
>
> spring.datasource.username=root
>
> spring.datasource.password=YOUR_MYSQL_PASSWORD
>
> spring.redis.host=localhost
>
> spring.redis.port=6379
>
> server.port=8082

**Step 6 --- Build the Application**

> mvn clean install -DskipTests

This compiles the project and packages it into a JAR file in the target/ directory.

**Step 7 --- Run the Application**

> mvn spring-boot:run

Or run the JAR directly:

> java -jar target/API-Rate-Limiting-Quota-Management-System-0.0.1-SNAPSHOT.jar

The application starts on http://localhost:8082

**📡 API Endpoints**

**Protected API Endpoints (require X-API-KEY header)**

|            |              |                                                    |
|------------|--------------|----------------------------------------------------|
| **Method** | **Endpoint** | **Description**                                    |
| **GET**    | /api/test    | Test authenticated access, returns userId & planId |

**Admin Endpoints (no auth required)**

|            |                          |                                        |
|------------|--------------------------|----------------------------------------|
| **Method** | **Endpoint**             | **Description**                        |
| **GET**    | /admin/users             | List all users with their plan & quota |
| **GET**    | /admin/plans             | List all subscription plans            |
| **GET**    | /admin/usage/{userId}    | Get today\'s usage stats for a user    |
| **GET**    | /admin/top-users?limit=5 | Top users by request count today       |
| **GET**    | /admin/blocked-requests  | Count of 401/429 blocked requests      |
| **GET**    | /admin/logs              | Full API access log                    |

**🧪 Testing with curl**

**Make an authenticated request**

> curl -H \"X-API-KEY: free-user-key-001\" http://localhost:8082/api/test

**Check usage for a user**

> curl http://localhost:8082/admin/usage/1

**Get top users**

> curl http://localhost:8082/admin/top-users?limit=5

**⚙️ How It Works**

**Request Lifecycle**

- Client sends request with X-API-KEY header to any /api/\* endpoint

- ApiKeyAuthFilter intercepts the request (Order 1, runs before controllers)

- Filter validates the API key against MySQL --- returns 401 if missing or invalid

- Redis counter is incremented: rate_limit:{userId}:{date}

- If counter \> dailyQuota → 429 Too Many Requests

- If counter \> softLimitThreshold → 300ms artificial delay applied

- Request is forwarded to the controller with USER_ID and PLAN_ID attributes

- ApiLogService asynchronously logs the endpoint, status, and timestamp to MySQL

**Rate Limit Key Format**

> rate_limit:{userId}:{YYYY-MM-DD}

Keys are stored in Redis and expire at midnight via the QuotaResetScheduler.

**📁 Project Structure**

|                               |                                             |
|-------------------------------|---------------------------------------------|
| **Package / File**            | **Responsibility**                          |
| config/AsyncConfig            | Enables @Async support                      |
| config/FilterConfig           | Registers ApiKeyAuthFilter for /api/\*      |
| config/RedisConfig            | Configures RedisTemplate\<String, Integer\> |
| filter/ApiKeyAuthFilter       | Core auth + rate limiting + logging filter  |
| service/RateLimitingService   | Redis increment & key management            |
| service/ApiLogService         | Async log writes to MySQL                   |
| service/AdminService          | Analytics: usage, top users, blocked count  |
| scheduler/QuotaResetScheduler | Clears Redis keys daily                     |
| entity/User                   | User entity with API key & plan             |
| entity/Plan                   | Plan entity with quota & soft limit         |
| entity/ApiLog                 | Request log entry                           |
| entity/ApiUsage               | Daily usage tracking                        |
| controller/TestController     | Protected /api/test endpoint                |
| controller/AdminController    | Admin analytics endpoints                   |
| controller/UserController     | User listing endpoint                       |
| controller/PlanController     | Plan listing endpoint                       |
| controller/ApiLogController   | Log retrieval endpoint                      |

**🛠️ Troubleshooting**

|                                      |                                                                     |
|--------------------------------------|---------------------------------------------------------------------|
| **Error**                            | **Fix**                                                             |
| Connection refused (Redis)           | Run redis-server and verify redis-cli ping returns PONG             |
| Access denied (MySQL)                | Check spring.datasource.username/password in application.properties |
| Port 8082 already in use             | Kill the process using lsof -i :8082 or change server.port          |
| 401 Missing API Key                  | Add -H \"X-API-KEY: your-key\" to your curl request                 |
| 429 Daily quota exceeded             | Wait until midnight or manually clear Redis with FLUSHDB            |
| Lombok errors in IDE                 | Enable annotation processing in your IDE settings                   |
| spring-boot-starter-webmvc not found | Ensure you are using Spring Boot 4.x in your POM                    |

**🔐 Environment Variables Reference**

|                    |                                                  |
|--------------------|--------------------------------------------------|
| **MYSQL_URL**      | jdbc:mysql://localhost:3306/api_rate_limiting_db |
| **MYSQL_USER**     | root (or your MySQL username)                    |
| **MYSQL_PASSWORD** | Your MySQL password                              |
| **REDIS_HOST**     | localhost                                        |
| **REDIS_PORT**     | 6379                                             |
| **SERVER_PORT**    | 8082                                             |

*Built with Spring Boot 4 · Java 21 · Redis · MySQL*
