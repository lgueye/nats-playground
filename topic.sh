#!/usr/bin/env bash

pkill -f nats

# build server
mvn clean install -f `pwd`/pom.xml

# 1. start nats server cluster (2 nodes)
gnatsd -config ./server-0.conf -D &
gnatsd -config ./server-1.conf -D &

# 2. start nats consumers cluster (2 nodes)
java -jar `pwd`/nats-consumer/target/*.jar --server.port=5000 --nats.server.urls=nats://localhost:4202,nats://localhost:4212 &
sleep 3s

java -jar `pwd`/nats-consumer/target/*.jar --server.port=5001 --nats.server.urls=nats://localhost:4202,nats://localhost:4212 &
sleep 3s

# 3. start nats producers cluster (1 node)
java -jar `pwd`/nats-producer/target/*.jar --server.port=4000 --nats.server.urls=nats://localhost:4202,nats://localhost:4212 &
sleep 3s

# 4. produce to topic
for i in {1..20}; do if [[ ${i}%2 -eq 0 ]]; then state="on"; else state="off"; fi; MESSAGE="{\"id\":\"`uuidgen`\",\"at\":\"`date -u +"%Y-%m-%dT%H:%M:%S.%3N"Z`\",\"state\":\"${state}\"}" ; curl -i -H 'Content-Type:application/json' -XPOST 'http://localhost:4000/topic/events' -d ${MESSAGE}; sleep 0.5s; if [[ ${i} == 10 ]]; then pkill -f "server-0.conf"; fi done
