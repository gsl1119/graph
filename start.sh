#!/bin/bash

set -x
set -e

id = docker ps | awk 'NR==2' | awk '{print $1}'
docker stop $id
docker rmi -f  graph/springboot:1.0.0
docker build -f dockerfile  -t  graph/springboot:1.0.0 .
docker run -d -p 8888:8888 graph/springboot:1.0.0
