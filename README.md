# Exploring Apache Camel

## Run Rabbit Run

Some of the trial code uses RabbitMQ, in order for it to work, you need a running RabbitMQ instance. One easy way to 
get one is to run it via Docker.

    docker run --hostname camel-rabbit-host --name camel-rabbit -p 15672:15672 -p 5672:5672 --rm rabbitmq:3-management 

This gives you rabbit running locally in a Docker container with the rabbit management site available at 
[http://localhost:15672](http://localhost:15672) and a username/password of guest/guest