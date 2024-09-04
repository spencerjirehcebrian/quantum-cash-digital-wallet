# Quantum Cash Project

![Quantum Dragons Logo](http://cdn.mcauto-images-production.sendgrid.net/b67df7af645e7fff/de205979-8c5b-4f34-aa70-150592a89cde/447x441.png)

Quantum Cash is a digital payment gateway system comprised of multiple microservices. This project demonstrates very basic financial transaction processing using Stripe, bank reconciliation, and user management in a distributed architecture.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Git
- Docker and Docker Compose
- Java JDK 17 or higher
- Maven

### Installation and Setup

1. Clone the repository:
   ```
   git clone https://github.com/stratpoint-training/java-quantum-dragons.git
   cd java-quantum-dragons
   ```

2. Start the main services using Docker Compose:
   ```
   docker-compose up -d
   ```

3. Navigate to each service directory and run the Spring Boot application:

   ```
   cd account-service
   mvn spring-boot:run
   ```
   
   Repeat this step for each of the following services (open a new terminal window for each):
   - bank-reconciliation-service
   - notification-service
   - payment-gateway-service
   - transaction-service
   - user-service

4. Once all services are running, execute the test script in the rrot directory:
   ```
   chmod +x quantum_cash_test.sh
   ./quantum_cash_test.sh
   ```

## Service Overview

- **account-service**: Manages user account information and balances
- **bank-reconciliation-service**: Handles reconciliation of transactions with external banks
- **notification-service**: Manages sending email notifications to users
- **payment-gateway-service**: Interfaces with external payment systems
- **transaction-service**: Processes and records all financial transactions
- **user-service**: Manages user authentication and profiles

## Running Tests

The `quantum_cash_test.sh` script runs a series of integration tests across all services to ensure the system is functioning correctly. It simulates various banking operations and verifies the results.

## Troubleshooting

- If you encounter any issues with Maven dependencies, try running `mvn clean install` in the root directory.
- Ensure all required ports are free before starting the services.
- Check Docker logs if any service fails to start properly.

## Acknowledgments

- Thank you to Sir Ronald and Sir Arjun for their guidance and inspiration.
- Thank you to Stratpoint for their wonderful internship program.
- Thank you to ChatGPT for its patience and mercy.
- Thank you again to Sir Ronald for his patience and mercy.
- Thank you to the Quantum Dragon Team for being amazing.
- Thank you to the Wyverns for their meetings.
- Thank you to the Electric Titans for their mappers.
