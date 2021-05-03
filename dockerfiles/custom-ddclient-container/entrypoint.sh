#!/bin/bash

# Subsitute environment variables in config file
envsubst < /defaults/ddclient.conf > /defaults/ddclient.conf.swp
mv /defaults/ddclient.conf.swp /defaults/ddclient.conf
cp /defaults/ddclient.conf /config/ddclient.conf
cp /defaults/ddclient.conf /ddclient.conf

# Entrypoint for linuxserver/docker-ddclient
/init


