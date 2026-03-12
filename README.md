# Reward Points API
## Overview

This project is a Spring Boot REST API that calculates reward points for customers based on their transactions.

**The reward calculation follows the below rules:**
- 2 points for every dollar over $100
- 1 point for every dollar between $50-$100
- 0 points below $50

Returns monthly rewards and total points for given customer.

## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- REST API

## Setup
### 1. Clone the Repository
git clone https://github.com/harshalshimpi001-png/Reward.git

cd Reward

### 2. Build the Project
mvn clean install

### 3. Run the Application
mvn spring-boot:run

Application will start on:
http://localhost:8081

## H2 Database Console
The project uses **H2 in-memory database**.

Access H2 console at:
http://localhost:8081/h2-console

#### Connection Details:
- JDBC URL: jdbc:h2:mem:rewardsdb
- Driver Class: org.h2.Driver
- Username: sa
- Password:
(Password is empty)

## API Endpoint
### 1. Get Reward Points for a Customer (Optional Date Range)

**GET** `/rewards/{customerId}?startDate={startDate}&endDate={endDate}`

**Path Parameter:**
- `customerId` (required) – ID of the customer

**Query Parameters (Optional):**
- `startDate` – Start date in `YYYY-MM-DD` format
- `endDate` – End date in `YYYY-MM-DD` format

***SampleRequest:***

`http://localhost:8081/rewards/1?startDate=2026-01-01&endDate=2026-03-10`

***Sample Response:***

`
{
"customerId": 1,
"customerName": "Harshal Shimpi",
"monthlyRewards": [
{
"month": 1,
"rewardPoints": 115,
"year": 2026
},
{
"month": 2,
"rewardPoints": 250,
"year": 2026
},
{
"month": 3,
"rewardPoints": 110,
"year": 2026
}
],
"totalRewardPoints": 475
}
`

## Running Tests
Run the unit tests using:
mvn test

## Sample Test Data
Initial transaction data is loaded using data.sql.