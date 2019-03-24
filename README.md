#### Build project 

```
./mvnw clean install
```


#### Build docker image

```
./mvnw install dockerfile:build
```


### Run docker image

```
docker run -p 8080:8080 virtuace/groupchat
```
