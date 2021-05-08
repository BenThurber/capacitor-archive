FROM tomcat:9.0

# Config
COPY ./server.xml /usr/local/tomcat/conf/server.xml

# Certificate
COPY  ./www_capacitor-archive_com.crt /usr/local/tomcat/ssl/server.crt

# Key
COPY  ./www_capacitor-archive_com.key /usr/local/tomcat/ssl/server.key

