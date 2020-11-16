#!/bin/bash

fuser -k 35503/tcp || true
pwd
java -jar test-server/libs/demo-0.0.1-SNAPSHOT.jar --server.port=35503
