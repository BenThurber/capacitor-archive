# The Capacitor Archive setup

This is a web application that will allow users to upload images historical information about vintage [electronic capacitors](https://en.wikipedia.org/wiki/Capacitor).  The format will be similar to that of Wikipedia or [radiomuseum.org](https://www.radiomuseum.org/tubes/tube_5z3.html) where there is a page for each historical articact (capacitor) with links to similar pages.

## Overview

This app uses an [Angular](https://angular.io/) front end and [Spring Boot](https://spring.io/projects/spring-boot) backend.  A DevOps methodology has been imployed through gitlab-runner's [CI/CD pipeline](https://docs.gitlab.com/ee/ci/) that performs automatic testing and deployment.

## Setting up CI/CD and MySQL using docker containers

This project uses gitlab-runner to perform Continuous Integration (automated building and testing) and Continuous Deployment of the capacitor-archive application.  The following steps should be performed on a continuously running machine (VM or bare metal) that can be accessed from the ports 80 and 8081 (phpMyAdmin).

### Install docker and docker-compose
Inside the machine, (either a VM or bare metal) install docker    
`sudo apt-get update`  
`sudo apt install docker.io`  

Start and enable docker  
`sudo systemctl start docker`  
`sudo systemctl enable docker`  

Install Docker Compose  
`sudo apt install docker-compose`  

### Set mysql credentials
Store the credentials for mysql in a file `mysql-variables.env` in the root directory of the project.  
```
MYSQL_ROOT_PASSWORD=<root-pass>
MYSQL_USER=<dba-username>
MYSQL_PASSWORD=<dba-password>
```


### Create a self signed certificate for phpMyAdmin
Create a directory to store certificates  
`sudo mkdir -p /etc/apache2/ssl`  
Create a self-signed certificate and answer the prompts.  The common name should be the domain name, i.e. capacitor-archive.com  
`sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/apache2/ssl/apache.key -out /etc/apache2/ssl/apache.crt`  


### Create docker containers for mysql, phpmyadmin and gitlab-runner
Run the following command which executes docker-compose.yml  
`sudo docker-compose build`  
`sudo docker-compose up -d`  


### Register the runner  
This should only have to be done once after running docker-compose for the first time.  
Open a shell inside the container.  
`sudo docker exec -it gitlab-runner bash`  
**Important** Set permissions on the following volumes  
`chown gitlab-runner /home/gitlab-runner/test-client/ /home/gitlab-runner/test-server/ /home/gitlab-runner/prod-client/ /home/gitlab-runner/prod-server/`  
And register with  
`gitlab-runner register`  


Follow the onscreen prompts:
- Enter the domain of your gitlab (https://gitlab.com/ if you are not hosing your own repository)  
- It will ask for a "gitlab-ci token".  Find this in your gitlab repository in the left bar under Settings > CI/CD > Runners.  Hit expand, and it is under the heading "Set up a specific Runner manually".  
- Enter a description  
- Enter any tags  
- Select shell  


## Accessing database and phpmyadmin
The command `sudo docker ps` can be used to see all running docker containers.  There should be three; phpmyadmin, capacitor-mysql and gitlab-runner.   
By default a database named `capacitor_test` is created inside the capacitor-mysql container.  
Phpmyadmin can be accessed from `https://<Host-Address>:8081/`  
Any container can be accessed through a shell using  
`sudo docker exec -it <container-name> bash`  


## Adding a firewall with ufw
Allow ssh  
`sudo ufw allow 22/tcp`  
Allow client  
`sudo ufw allow 35504/tcp`  
Allow server  
`sudo ufw allow 35503/tcp`  
Allow phpmyadmin  
`sudo ufw allow 8081/tcp`  
Allow phpmyadmin to connect to database  
`sudo ufw allow 3306/tcp`  

Turn on the firewall  
`sudo ufw enable`  

**IMPORTANT** Fix an exploit where docker bypasses ufw's firewall.  (See the issue [here](https://github.com/docker/for-linux/issues/690#issuecomment-529319051))  
`curl -s https://gist.githubusercontent.com/rubot/418ecbcef49425339528233b24654a7d/raw/22bc857b97e63fa65eb4b89d2b2745289a51641c/docker_ufw_setup.sh | sudo bash`  





