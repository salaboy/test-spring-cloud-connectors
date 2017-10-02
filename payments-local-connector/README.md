# test-payment-connector

This project provides a simple connector for the test Payment service that can be found here:

When you run it you can access it via: http://localhost:8080/payment


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
