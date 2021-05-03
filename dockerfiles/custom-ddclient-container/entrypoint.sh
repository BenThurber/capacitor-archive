#!/bin/bash

if [ "$1" = "set-password" ]; then

	echo "Hello World"
	#find / -name "ddclient.conf"
	#envsubst < /config/ddclient.conf > ddclient.conf.swp
	#mv ddclient.conf.swp /config/ddclient.conf

	for i in $(find -name ddclient.conf); 
	do
		echo $i
    		envsubst < $i > ddclient.conf.swp
        	mv ddclient.conf.swp $i
	done


fi

sleep infinity


