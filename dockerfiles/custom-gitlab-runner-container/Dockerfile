FROM gitlab/gitlab-runner:alpine


##############
# Install Node
##############


# Also installs Node as a dependency of npm
RUN echo "Installing Node" \
 && apk add --no-cache --repository http://dl-cdn.alpinelinux.org/alpine/v3.12/main/ npm \
 && node -v \
 && npm install -g @angular/cli \
 && npm install -g http-server


###############
# Install JDK11
###############


RUN echo "Installing JDK11" \
 && apk add --no-cache --repository=http://dl-cdn.alpinelinux.org/alpine/edge/community openjdk11 \
 && java --version



#########################
# Install Headless Chrome
#########################


# Installs latest Chromium package.

RUN echo "Installing Headless Chrome" && apk add chromium

ENV CHROME_BIN=/usr/bin/chromium-browser \
    CHROME_PATH=/usr/lib/chromium/



##################
# Install envsubst
##################

RUN echo "Installing other tools" \
 && apk add gettext

