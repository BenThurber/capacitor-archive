#!/bin/bash

echo "Hello World"
#find / -name "ddclient.conf"
#envsubst < /config/ddclient.conf > ddclient.conf.swp
#mv ddclient.conf.swp /config/ddclient.conf

for i in $(find -name ddclient.conf);
do
	echo "File:"
	echo $i
	grep "^password" $i
	envsubst < $i > ddclient.conf.swp
	mv ddclient.conf.swp $i
	grep "^password" $i
done

/init


