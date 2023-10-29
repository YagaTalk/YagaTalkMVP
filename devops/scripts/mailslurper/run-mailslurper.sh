#!/bin/sh

# Important $PWD/config.json mounting is not working on Windows machine
docker run \
    -v $PWD/config.json:/opt/mailslurper/config.json \
    -p 2500:2500 -p 8090:8080 -p 8085:8085 \
    marcopas/docker-mailslurper
