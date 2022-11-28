#!/usr/bin/env bash

rm -rf ./artifact 2>/dev/null
mvn clean package -U
docker run -it --rm -p 8080:8080 -v "$(pwd)/artifact:/usr/local/tomcat/webapps/" eclipse/starter-ui;
