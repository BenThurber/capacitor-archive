# Overview

**View the site at [capacitor-archive.com](http://www.capacitor-archive.com)**  
(And the test site [here](http://www.capacitor-archive.com:35506) from the development branch)  


This app uses an [Angular](https://angular.io/) front end and [Spring Boot](https://spring.io/projects/spring-boot) backend.  A DevOps methodology has been employed through gitlab-runner's [CI/CD pipeline](https://docs.gitlab.com/ee/ci/) that performs automatic testing and deployment.

# The Capacitor Archive setup
<sup>**If visiting from github please come to [gitlab.com](https://gitlab.com/capacitor-archive/capacitor-archive)**</sup>

The web application capacitor-archive.com will allow users to upload images and historical information about vintage [electronic capacitors](https://en.wikipedia.org/wiki/Capacitor).  The format will be similar to that of Wikipedia or [radiomuseum.org](https://www.radiomuseum.org/tubes/tube_5z3.html) where each vintage component (capacitor) has a page with links to similar pages.  **It is the hope of the devs that this website will encourage the documentation of vintage electronic components, and aid in the creation of reproductions for radio and guitar restorations.**  

Please see the [backlog on the Wiki](https://gitlab.com/capacitor-archive/capacitor-archive/-/wikis/home) for a full list of features.  

## Overview

This app uses an [Angular](https://angular.io/) front end and [Spring Boot](https://spring.io/projects/spring-boot) backend.  A DevOps methodology has been employed through gitlab-runner's [CI/CD pipeline](https://docs.gitlab.com/ee/ci/) that performs automatic testing and deployment.

## Setup a server to run  CI/CD and MySQL using docker containers

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

### Set credentials for mysql and mysql-backup
Create an environment variable file called `mysql.env` in the root directory of the project. This will be used by docker-compose to create the two mysql database containers.
```
MYSQL_ROOT_PASSWORD=<root-pass>
MYSQL_USER=<dba-username>
MYSQL_PASSWORD=<dba-password>
```
Create another environment variable file `mysql-backup.env` that stores the location and credentials of where to backup the two databases.  The variable DB_DUMP_TARGET could be a smb or [s3 server](https://aws.amazon.com/s3/) (e.g. s3://mysql-backups). The database dump filename is in the format <test|prod>_db_backup_YYYY-MM-DDTHH:mm:ssZ.<compression>.  
See the [databack/mysql-backup documentation](https://hub.docker.com/r/databack/mysql-backup) for more info.  
```
DB_PASS=<root-pass>
DB_DUMP_TARGET=<server URL>
AWS_ACCESS_KEY_ID=<Amazon Web Services Key Id>
AWS_SECRET_ACCESS_KEY=<Amazon Web Services Key>
AWS_DEFAULT_REGION=<Amazon Web Services Region>
```
NOTE: DB_PASS is the same as MYSQL_ROOT_PASSWORD above.  



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


## Create an Amazon Web Services (AWS) S3 server
The web app uses an S3 server to upload and serve images and other media.  This can be the same server that is used to backup mysql.  
Since there are several config files to add to the server, please see the page [Configure AWS S3 Server](https://gitlab.com/capacitor-archive/capacitor-archive/-/wikis/Configure-AWS-S3-Server) on the  Wiki for how to do this.  


## Add environment variables to GitLab
In GitLab, in the left bar, go to settings > CI / CD > Variables.  Set the variables:  
- SPRING_DATASOURCE_USERNAME  
- SPRING_DATASOURCE_PASSWORD  
(The same values as were used when [setting mysql credentials](#set-mysql-credentials))  

The keys for the AWS S3 server used for for hosting images and media.  These can be the same keys used in mysql-backup.env (see above)  
- AWS_SECRET_ACCESS_KEY  
- AWS_ACCESS_KEY_ID  

The keys from Google's reCAPTCHA API  
- RECAPTCHA_SITE_KEY  
- RECAPTCHA_SECRET_KEY  
<sub>(If unknown, the development keys can be used: 6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI and 6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe)</sub>  


## Accessing database and phpmyadmin
Phpmyadmin can be accessed from `https://<Host-Address>:8081/`  
Any container can be accessed through a shell using  
`sudo docker exec -it <container-name> bash`  
The container prod-mysql has a database named capacitor_prod.  
The container test-mysql has a database named capacitor_test.  


## Adding a firewall with ufw
Allow ssh  
`sudo ufw allow 22/tcp`  
Allow server and client  
`sudo ufw allow 35503:35506/tcp`  
Allow phpmyadmin  
`sudo ufw allow 8081/tcp`  
Allow phpmyadmin to connect to test and prod databases  
`sudo ufw allow 3306:3307/tcp`  

Turn on the firewall  
`sudo ufw enable`  

**IMPORTANT** Fix an exploit where docker bypasses ufw's firewall.  (See the issue [here](https://github.com/docker/for-linux/issues/690#issuecomment-529319051))  
`curl -s https://gist.githubusercontent.com/rubot/418ecbcef49425339528233b24654a7d/raw/22bc857b97e63fa65eb4b89d2b2745289a51641c/docker_ufw_setup.sh | sudo bash`  


## Port mappings

| service       | port  |
|---------------|-------|
| ssh           | 22    |
| prod-database | 3306  |
| test-database | 3307  |
| phpmyadmin    | 8081  |
| prod-server   | 35503 |
| prod-client   | 35504 |
| test-server   | 35505 |
| test-client   | 35506 |


