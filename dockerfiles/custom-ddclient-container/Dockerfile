FROM linuxserver/ddclient

COPY ./ddclient.conf /defaults/ddclient.conf


##################
# Install envsubst
##################
RUN echo "Installing envsubst" \
 && apk add gettext
##################


COPY entrypoint.sh /sbin/entrypoint.sh

RUN chmod +x /sbin/entrypoint.sh

ENTRYPOINT /sbin/entrypoint.sh


