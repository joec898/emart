#README.md -- api-gateway

##KEYCLOAK -- identity and access management setup

### start keycloak on docker
get command from:
    https://www.keycloak.org/getting-started/getting-started-docker
    
docker run -p 9090:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:20.0.3 start-dev

when stated:
http://localhost:9090/
adminitrasion sonsole, login using given admin/admin


Under the master realm, select Create Realm:
http://localhost:9090/admin/master/console/#/master/add-realm
Realm name = emart-microservices-realm

on dropdown menu, select Realms Settings
the emart-microservices-realm is created

click on Client menu, then Create Client button to create a new client

enter emart-realm-client

next

Set Client authentication on

un-check Standard flow and Direct access grants

check Service accounts roles  

save

on Credential of emart-realm-client screen

Save generated secret, which will be use for client access to emart-microservices-realm

on Realm setting page
click to open OpenID Endpoint Configuration:

http://localhost:9090/admin/master/console/#/emart-microservices-realm/realm-settings

on which you can find all end points for authorization uses

### add oauth2 for api-gateway
* add dependency:

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency> 
```
* add to application.properties:

spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:9090/realms/emart-microservices-realm

* add SecurityConfig to the project

### setup Zipkin distributed tracing
 
* from https://zipkin.io/pages/quickstart copy and run:
docker run -d -p 9411:9411 openzipkin/zipkin 

* add the line in application.properties file:
spring.zipkin.base-url=http://localhost:9411
spring.sleuth.sampler.probability= 1.0

### Keycloak in Docker and host name mapping in hosts file
Need to add this command to docker-compose file to import existing configurations 
of current Keycloak. It will save configuration data as realms/realm-export.json.
The data will be loaded in new keycloak deployment. The Client Secret in new Keycload needs to be regenerated.

```
command: [ "start-dev", "--import-realm" ]
```

When using Keycloak in docker, the url used in generate token (in postman):
http://localhost:9090/realms/emart-microservices-realm/protocol/openid-connect/token 
needs to be http://keycloak:9090/...

This can be mapped in .hosts file in C:\Windows\System32\drivers\etc folder
add a line like: 127.0.0.1       keycloak:

```
#	127.0.0.1       localhost
#	::1             localhost
127.0.0.1       keycloak
```










