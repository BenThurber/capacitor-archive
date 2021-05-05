FROM httpd:alpine

# Config
COPY ./my-httpd.conf /usr/local/apache2/conf/httpd.conf

# Certificate
COPY ./www_capacitor-archive_com.crt /usr/local/apache2/conf/server.crt

# Key
COPY ./www_capacitor-archive_com.key /usr/local/apache2/conf/server.key

