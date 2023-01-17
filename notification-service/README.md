##README.md -- notification service

### set up Kafka broker

* On https://developer.confluent.io/quickstart/kafka-docker/

copy the docker-compose.yml content to create a docker-comose.yml under emart

*  run docker-compose up -d in terminal under root folder (emart), exam the log

### Kafka Configuration
* copy mvn dependency from:
https://docs.spring.io/spring-kafka/docs/current/reference/html/

```
<dependency>
  <groupId>org.springframework.kafka</groupId>
  <artifactId>spring-kafka</artifactId>
  <version>3.0.2</version>
</dependency>
```

* add Kafka properties to application.propertites file of notification-service:

```
# Kafka Properties, consumer, deserializer
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.template.default-topic=notificationTopic
spring.kafka.consumer.group-id= notificationId
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.type.mapping=event:com.programming.techie.OrderPlacedEvent
```

* add Kafka properties to application.propertites file of order-service:

```
# Kafka Properties -- producer, serializer
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.template.default-topic=notificationTopic
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.properties.spring.json.type.mapping=event:com.programmingtechie.orderservice.event.OrderPlacedEvent
```
