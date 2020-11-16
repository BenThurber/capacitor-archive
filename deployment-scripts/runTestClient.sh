#!/bin/bash

fuser -k 35504/tcp || true
http-server ./test-client/dist/client/ -p 35504
