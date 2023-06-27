# R.E.S.T API

## Description

This is a simple REST API that allows you to create, read, update and delete;

## Technologies used:

- JDK 17 (Java 17)
- Hibernate (JPA Provider)
- Javalin (Web Framework)
- PostgresSQL (Database)
- Maven (Dependency Management)
- Docker (Containerization)
- Docker Compose (Container Orchestration)
- JUnit (Unit Testing)
- Mockito (Mocking Framework)
- Log4j (Logging Framework)
- Testcontainers (Integration Testing)
- Rest Assured (API Testing)

### Prerequisites

- JDK 17
- Docker
- Docker Compose
- Maven
- Git
- Postman (Optional)

### Deployment pipeline

1. Change docker image name in GitHub actions file

```yaml
        ......
        
        with:
          context: .
          file: ./Dockerfile
          push: true
          tags: ${{ secrets.DOCKERHUB_USERNAME }}/<your_api_name>:latest
```

2. Make sure yoy have the following secrets in your GitHub repository

- DOCKERHUB_USERNAME
- DOCKERHUB_TOKEN

3. Update the GitHub action file to match your branch name you want to build on

```yaml
on:
  push:
    branches: [ branches_to_build ]

    ......
```

4. Update the pom file with the right properties

```xml
    <properties>
        
        ....
    
        <!-- Project properties  -->
        <!--  token    -->
        <secret.key>your secret key</secret.key>
        <issuer>your domain</issuer>
        <token.expiration.time>3600000</token.expiration.time>
        <!--  DB    -->
        <db.name>database name</db.name>
        <db.username>database username</db.username>
        <db.password>database password</db.password>
        <db.connection.string>jdbc:postgresql://localhost:5432/</db.connection.string>
        <!--  Javalin    -->
        <javalin.port>port number</javalin.port>
    </properties>
```
