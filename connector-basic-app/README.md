# test-spring-cloud-connectors

This project is a reproducer for testing Spring Cloud Service Connectors.

The test inside the application send a message that is picked up by an @StreamListener which then loads the cloud connector from the cloud provider
and execute the interaction with the external service. Once the service is executed and we have a result we produce an IntegrationResult and the test check
that we consume that result with the correct values.   

This test uses RabbitMQ & a payment service that's why you need to make sure that these services are started (Docker Compose provided)

In order to configure your cloud connectors you need to: 

1) create a file: /tmp/myApp/spring-cloud.properties
(this is specified here: spring-cloud-bootstrap.properties if you want to change it)
2) With the following content: 

spring.cloud.appId=myApp
spring.cloud.payment=payment://localhost:8081/payment


In order to run the application run: 

1) cd docker/
2) docker-compose up -d (this start rabbit-mq and the payment service)
3) cd ..
4) mvn clean install 

optional) you can do curl http://localhost:8081/payment to get the processed payments from the payment service