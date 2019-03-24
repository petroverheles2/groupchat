#### Build project 

```
./mvnw clean install
```

#### Run the application with maven
```
./mvnw spring-boot:run
```

#### Build docker image

```
./mvnw install dockerfile:build
```

### Run docker image

```
docker run -p 8080:8080 virtuace/groupchat
```
