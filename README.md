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
