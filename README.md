Quarkus, the Supersonic Subatomic Java Framework.
This project uses MongoDB with Panache with reactive endpoints by creating/updating entities

Key characteristics of reactive endpoints include:

1. Asynchronous: Reactive endpoints use asynchronous processing to handle requests. Instead of blocking a thread while
   waiting for a response, the server can immediately move on to handle other requests, making more efficient use of
   resources.

2. Non-blocking: Reactive endpoints avoid blocking threads for I/O operations, such as reading from databases or making
   external API calls. Non-blocking I/O allows the server to handle more requests with fewer threads, reducing resource
   consumption.

3. Reactive Streams: Reactive endpoints often use Reactive Streams, a specification that defines a set of interfaces for
   asynchronous stream processing with non-blocking backpressure. Reactive Streams allow components in the system to
   communicate efficiently, enabling flow control to handle varying data rates between producers and consumers.

4. Event-Driven: Reactive endpoints are event-driven, meaning they respond to events as they occur, such as new requests
   arriving or data becoming available. This allows for responsiveness and scalability.

5. Functional Programming: Reactive programming often involves the use of functional programming concepts, such as
   higher-order functions, lambdas, and immutability. These concepts make it easier to compose and manage asynchronous
   operations.

To run the project, start docker with mongo
You can also create/update/delete creditcards in mongo via the endpoints
You can process the creditcards and it will take the credit cards and insert in mongo and encrypt the card number as
well

## Running the application in dev mode with live coding

./mvnw compile quarkus:dev or mvn quarkus:dev

## Run mongodb in Docker container first  git

    docker run -ti --rm -p 27017:27017 mongo:4.0

## GET All Creditcards

    $ curl "localhost:8080/creditcards"

## Example Response get all credit cards

## GET All creditcards by number and cardHolderName

    $ curl "localhost:8080/creditcards/search?number=6011056413549024"

## GET All creditcards by date of expiry

    $ curl "localhost:8080/creditcards/search?dateFrom=2021-06-17T00:00:00.000Z&dateTo=2022-06-17T00:00:00.000Z"

## GET All creditcards by multiple users

    $ curl "localhost:8080/creditcards/search2?authors=Hemraj"

## Create Creditcard

    $ curl -X POST "localhost:8080/creditcard"
    
    {
    "cardNumber":"55528222232333",
    "cardHolderName":"Hemraj",
	"expiryDate":"22/24",
    }

## delete creditcard by id

    $ curl -X DELETE "http://localhost:8080/creditcards/64c3a4aeb63aed2624f07d14"

## Change the filename  creditcards.json or creditcards.xml  in ReactiveCreditCardResource.java on line 80

    creditCardsFileName = "creditcards.csv";    
    Process the credit cards, only valid cards will be saved 
    Go to http://localhost:8080/creditcards to view the results 
    Some data will be encrypted with  AESEncryption.

## Process credit cards :

$ curl -X POST localhost:8080/creditcards/process

## For authentication, we must start keycloak on docker, though it is still a work in progress and the details are located here :

## Keycloak

    docker run --name keycloak -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -p 8543:8443 -v "$(pwd)"/config/keycloak-keystore.jks:/etc/keycloak-keystore.jks quay.io/keycloak/keycloak:17.0.0 start-dev  --hostname-strict=false

## Keycloak dev url : http://localhost:8080/q/dev/io.quarkus.quarkus-vertx-http/config?filterByExtension=Keycloak%20Authorization&filterConfigKeys=quarkus.oidc.,quarkus.keycloak.

## quarkus.oidc.auth-server-url  : http://localhost:51619/realms/quarkus

