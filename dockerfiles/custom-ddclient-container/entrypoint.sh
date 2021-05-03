#!/bin/bash

echo "Hello World"

envsubst < /defaults/ddclient.conf > /defaults/ddclient.conf.swp
mv /defaults/ddclient.conf.swp /defaults/ddclient.conf
cp /defaults/ddclient.conf /config/ddclient.conf
cp /defaults/ddclient.conf /ddclient.conf

for i in $(find -name ddclient.conf);
do
	echo "File:"
	echo $i
	grep "^password" $i
	#envsubst < $i > ddclient.conf.swp
	#mv ddclient.conf.swp $i
	#grep "^password" $i
done

/init


