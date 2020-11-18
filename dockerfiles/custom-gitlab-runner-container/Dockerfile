FROM gitlab/gitlab-runner:ubuntu

# Expose server and client ports
EXPOSE 35503 35504

##############
# Install Node
##############


# Also installs Node as a dependency of npm
RUN echo "Installing Node" \
 && apt-get install npm \
 && node -v \                     # Works with 6.14.6 
 && npm install -g @angular/cli \
 && npm install -g http-server


###############
# Install JDK11
###############


RUN echo "Installing JDK11" \
 && sudo apt-get install openjdk-11-jdk
 && java --version               # Works with 11.0.9



#########################
# Install Headless Chrome
#########################


# Installs latest Chromium package.

RUN echo "Installing Headless Chrome" \
 && apt-get install chromium-browser \
 && chromium-browser --version           # Works with 86.0.4240.111 

ENV CHROME_BIN=/usr/bin/chromium-browser \
    CHROME_PATH=/usr/lib/chromium/



