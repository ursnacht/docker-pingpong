#!/bin/sh
mvn clean package && docker build -t docker-se.glue.ch/znueni .
docker rm -f znueni || true && docker run -d -p 8080:8080 -p 4848:4848 --name znueni docker-se.glue.ch/znueni 
