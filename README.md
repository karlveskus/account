# Account service

The Account Service manages accounts, balances, and transactions.

## Setup and Running Instructions

### Running the service

To start the service, run the following command to initialize the required Docker containers:

```bash
docker-compose up -d
```
This command sets up a local PostgreSQL database and RabbitMQ, and it also starts the service.

Access the service at [http://localhost:8080](http://localhost:8080)

## Important choices
1. Using BIGINT and cents as the amount to support any currency. For example
   2. EUR, USD have 2 decimal points
   3. JPY has no decimal places
   4. BTC has 8 decimal places
2. Transactions are related to the account, not the balance, making it faster to query by accountId.
3. Three separate RabbitMQ exchanges (account, balance, transaction) are used to segregate events by type for consumers.
4. Optimistic locking is implemented for balance updates to ensure safety with multiple threads or instances attempting to access and update it simultaneously.
5. ErrorCode is added to error responses to provide better understanding of what went wrong in the service call.

## Improvements/considerations for horizontal scaling
1. Implement a load balancer in front of application instances
2. Use CPU/Memory-based auto-scaling mechanisms
3. Implement primary-replica pattern for database instances. Separate read-only instances for read operations that accept eventual consistency

