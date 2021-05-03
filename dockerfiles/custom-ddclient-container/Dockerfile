FROM linuxserver/ddclient

#COPY ./ddclient.conf /config/ddclient.conf
#COPY ./ddclient.conf /etc/ddclient/ddclient.conf
COPY ./ddclient.conf /defaults/ddclient.conf

#RUN sed -i -e "s/DYNAMIC_DNS_PASSWORD/$DYNAMIC_DNS_PASSWORD/g" /etc/ddclient/ddclient.conf



##################
# Install envsubst
##################
RUN echo "Installing envsubst" \
 && apk add gettext
##################

RUN echo $NAMECHEAP_PASSWORD

ADD entrypoint.sh /sbin/entrypoint.sh

RUN chmod +x /sbin/entrypoint.sh

CMD ["/sbin/entrypoint.sh", "set-password"]


