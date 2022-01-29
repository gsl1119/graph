#!/bin/bash

set -x
set -e

docker stop $(docker ps -a -q)
docker rmi -f  graph/springboot:1.0.0
docker build -f dockerfile  -t  graph/springboot:1.0.0 .
docker run -d -p 8888:8888 graph/springboot:1.0.0
