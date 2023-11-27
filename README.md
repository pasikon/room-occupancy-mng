Hello, this is Spring Boot 3 Room Occupancy Manager application.

The project uses Maven, Java 17 is required to run it.

To compile and run the tests please run:

```shell
mvn clean compile test
```

In order to start the application and interact with REST facade please run:

```shell
mvn spring-boot:run
```

The REST facade is exposed on port 8080, you can interact with the price calculation endpoint using `curl`:

```shell
curl --location --request GET 'http://localhost:8080/calculateOccupancyWithPrice' \
--header 'Content-Type: application/json' \
--data '{
    "freeEconomyRooms" : 3,
    "freePremiumRooms" : 3,
    "customerData" : [23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]
}'
```