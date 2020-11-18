FROM gitlab/gitlab-runner:ubuntu

# Expose server and client ports
EXPOSE 35503 35504


##############
# Install Node
##############


# Also installs Node as a dependency of npm
RUN echo "Installing Node" \
 && apt-get update \
 && apt-get install -y git-core curl build-essential openssl libssl-dev python \
 && git clone https://github.com/nodejs/node.git
 && cd node
 # Works with v12.18.4 
 && node -v \
 && npm install -g @angular/cli \
 && npm install -g http-server


###############
# Install JDK11
###############


RUN echo "Installing JDK11" \
 && sudo apt-get install openjdk-11-jdk \
 # Works with 11.0.9
 && java --version



#########################
# Install Headless Chrome
#########################


# Installs latest Chromium package.

RUN echo "Installing Headless Chrome" \
 && apt-get install chromium-browser \
 # Works with 86.0.4240.111
 && chromium-browser --version

ENV CHROME_BIN=/usr/bin/chromium-browser \
    CHROME_PATH=/usr/lib/chromium/



