#  Raffle System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green.svg)](https://spring.io/projects/spring-boot) [![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/) [![Redis](https://img.shields.io/badge/Redis-7.0-red.svg)](https://redis.io/) [![DDD](https://img.shields.io/badge/Architecture-DDD-orange.svg)]()

>  **Introduction**:
> **Big Market** is a high-performance, scalable distributed marketing raffle system. The project adopts **DDD (Domain-Driven Design)** architecture, aiming to handle complex marketing scenarios (e.g., point redemption, lucky wheels, red envelope rain). The system core decouples **raffle strategies** from the **activity lifecycle** and extensively uses design patterns (Template, Strategy, Composite, Chain of Responsibility) to manage dynamic business rules.

---

##  Architecture & Philosophy

This project strictly follows the **DDD Layered Architecture**, encapsulating core logic within the Domain layer to ensure purity and testability.

### 1. System Layering

- **`big-market-app` (Application Layer)**: The entry point of the system. It orchestrates business processes but contains no core business logic.
- **`big-market-trigger` (Trigger Layer)**: Exposes interfaces (HTTP/RPC) and handles incoming messages (MQ Listeners/Scheduled Jobs).
- **`big-market-domain` (Domain Layer)**: **The Core**. Contains aggregates such as `Strategy` (Raffle algorithms), `Activity` (Activity lifecycle), and `Award` (Prize definitions).
- **`big-market-infrastructure` (Infrastructure Layer)**: Handles database interactions (DAO), Redis caching, and external service calls.

### 2. Key Design Patterns
To handle complex raffle rules (e.g., blacklists, weighted draws, fallback strategies), the project heavily utilizes design patterns:

* **Template Method Pattern**:
    * *Usage*: Defines the standard skeleton of the raffle process (Param Validation -> Rule Filtering -> Algorithm Execution -> Result Packaging).
    * *Value*: Unifies the main logic flow while allowing subclasses to implement specific algorithm details.
* **Strategy Pattern**:
    * *Usage*: Handles different raffle algorithms (e.g., Total Probability vs. Single Item Probability).
    * *Value*: Allows dynamic switching of algorithms and easy extension for new raffle methods.
* **Chain of Responsibility Pattern**:
    * *Usage*: Used for pre-raffle rule filtering. (e.g., Check Blacklist -> Check Weight Rules -> Default to General Pool).
    * *Value*: Decouples complex validation logic, allowing flexible assembly of rule chains.
* **Composite Pattern / Decision Tree**:
    * *Usage*: Manages complex Rule Trees for dynamic logic decision-making.
    * *Value*: Abstracts the decision process into a tree traversal, significantly increasing the flexibility of the rule engine.

---

##  Core Workflows

### Raffle Strategy Flow
The system supports highly configurable raffle strategies:

1.  **Strategy Assembly**: Operations staff configure raffle strategies (probabilities, prizes, rules).
2.  **Weight Rules**: Dynamically adjust winning probabilities based on user points or tags (e.g., VIP users are guaranteed to draw from a high-value prize pool).
3.  **Stock Deduction**: Uses Redis + Lua scripts or Database Optimistic Locking to ensure safe stock deduction under high concurrency.

## Getting Started

### Prerequisites

- JDK 1.8+ / 17+
- Maven 3.6+
- Docker (Optional, for middleware)
- MySQL 8.0
- Redis

### 1. Database Initialization

Please execute the SQL scripts located in the `/docs/sql` directory to initialize the necessary tables:

- `strategy`, `strategy_rule`, `strategy_award`: Strategy-related tables.
- `raffle_activity`, `raffle_activity_account`: Activity and account tables.

### 2. Configuration

Modify `big-market-app/src/main/resources/application.yml`:



```YAML
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/big_market?useUnicode=true...
    username: root
    password: your_password
  redis:
    host: 127.0.0.1
    port: 6379
```

### 3. Build & Run


```Bash
# In the root directory
mvn clean install

# Start the application
java -jar big-market-app/target/big-market-app.jar
```

### 4. API Test (CURL Example)

Example of calling the raffle interface (adjust based on actual Controller path):



```Bash
curl -X POST http://localhost:8091/api/v1/raffle/strategy/random_raffle \
-H "Content-Type: application/json" \
-d '{
    "strategyId": 10001
}'
```
### 5. Database and Redis Launched by Docker
docker compose file has already configured mysql and redis.\
The project can be launched by docker file `docs/dev-ops/docker-compose-lite.yml` easily.
```bash
# start up, in the project root path
docker-compose -f ./docs/dev-ops/docker-compose-lite.yml up -d
# close
docker-compose -f ./docs/dev-ops/docker-compose-lite.yml down

```

------

## Project Structure

Plaintext

```
big-market
├── big-market-api            # RPC Interfaces, DTOs, Result objects
├── big-market-app            # Application entry, Config, Packaging
├── big-market-domain         # [Core] Domain Layer: Strategy, Rules, Raffle Logic
│   ├── activity              # Activity Domain
│   ├── award                 # Award Domain
│   ├── strategy              # Strategy Domain (Core Algorithms)
│   └── task                  # Task Domain
├── big-market-infrastructure # Infra: DAO, Redis, RocketMQ
├── big-market-trigger        # Trigger: Http Controller, Jobs, MQ Listeners
└── big-market-types          # Common Types, Enums, Exceptions
```

## Roadmap

- [x] Basic Raffle Strategy Domain Implementation
- [x] Standardize Raffle Flow with Template Pattern
- [x] Decision Tree Engine Implementation
- [ ] Integrate Kafka for Asynchronous Prize Distribution
- [ ] ShardingSphere Support (Database Sharding)
- [ ] Frontend Demo Page Development