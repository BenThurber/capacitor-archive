FROM phpmyadmin:5.0.2-apache

RUN a2enmod ssl

RUN sed -ri -e 's,80,443,' /etc/apache2/sites-available/000-default.conf \
 && sed -i -e '/^<\/VirtualHost>/i ServerName capacitor-archive.com' /etc/apache2/sites-available/000-default.conf \
 && sed -i -e '/^<\/VirtualHost>/i SSLEngine on' /etc/apache2/sites-available/000-default.conf \
 && sed -i -e '/^<\/VirtualHost>/i SSLCertificateFile /certs/phpmyadmin.crt' /etc/apache2/sites-available/000-default.conf \
 && sed -i -e '/^<\/VirtualHost>/i SSLCertificateKeyFile /certs/phpmyadmin.key' /etc/apache2/sites-available/000-default.conf

EXPOSE 443
