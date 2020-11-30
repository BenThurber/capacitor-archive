FROM tomcat:9.0

# 8080 is the default if no value is given
ARG port=8080

EXPOSE $port

RUN sed -i "s/8080/$port/" /usr/local/tomcat/conf/server.xml

