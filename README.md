# AS2/4 Gateway

## Pre-requisites
To get started you will need to have the following installed:
  * Java 8
  * Docker

## Description
Gateway for incoming AS2 and AS4 traffic. This codebase consists both of the backend written in Java Spring Boot as well as the frontend written in React. Maven is used a the build tool and and it comes bundled in the project, it packages the full stack.

## Running the first time
The easiest way to get started is to just start the service from the commandline using the "local" spring boot profile. This will effectively disable all external dependencies like consul and non-in-memory databases. Due to the dependency on external services running it with this profile is suited for some development work as well as testing. You can run it using:

```
./mvnw clean spring-boot:run -Dspring.profiles.active=local
```

you can also compile a fat jar easily just by running:

```
./mvnw clean install
```

## Folder structure
The folder structure is standard with the exception of the frontend. Below some highlighted areas of the code:

    .
    ├── src                 
    │   ├── main
    │   │   ├── client      # Frontend written in React
    │   │   ├── java        # Backend written in Spring Boot
    │   │   └── resources   # Resources for the Spring Boot app, including the bundled React app
    │   └── test
    │   │   ├── java        # Backend tests
    │   │   └── resources   # Test specific resources
    └── README.md
    
## Running the development cluster
You can run the full development cluster using docker-compose up. When you run this all development versions of all microservices that the AS2/4 gateway along with the gateway itself will spin up in a local docker-compose cluster. To do this run:

```
docker-compose up --build
```
