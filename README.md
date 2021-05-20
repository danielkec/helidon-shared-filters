# Reusing Helidon SE filter in MP

Helidon MP provides a way of configuring reactive routing over Helidon SE api.
https://helidon.io/docs/v2/#/mp/jaxrs/10_reactive-routing

This is an example of creating Helidon SE server filter and reusing it in Helidon MP.

## Build and run

With JDK11+
```bash
mvn clean install && java -jar ./cooled-service-mp/target/cooled-service-mp.jar;
```

## Exercise the application

[CoolingSEFilterService](cooled-service-se/src/main/java/me/kec/se/bare/CoolingSEFilterService.java) 
checks every response if it contains `Cool-Header`, if it does it set a header `Cool-Header` to response with original header value prefixed with `This is way cooler response than `.



```
curl -i -H "Cool-Header: cool enough?" http://localhost:8080/greet

HTTP/1.1 200 OK
Content-Type: application/json
Cool-Header: This is way cooler response than cool enough?
Date: Thu, 20 May 2021 10:53:01 +0200
connection: keep-alive
content-length: 26

{"message":"Hello World!"}
```