# The Capacitor Archive setup

## Setting up a VM for CI/CD and MySQL using docker containers

### Install docker
Inside a linux machine, (either a VM or bare metal) install docker  
`sudo apt-get update`  
`sudo apt install docker.io`

Start and enable docker  
`sudo systemctl start docker`  
`sudo systemctl enable docker`


### Installing mysql inside docker

Install and start mysql 8.0.22 Inside docker.  Replace <password> with your password:  
`sudo docker run --name capacitor-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=<password> -d mysql:8.0.22`

Get a terminal inside the capacitor-mysql docker container  
`sudo docker exec -it capacitor-mysql bash`

Login to mysql with  
`mysql -u root -p`

Create a new database  
`CREATE DATABASE capacitor_test;`

Add a new user with password and grant them privileges on all databases  
`CREATE USER '<New User>'@'%' IDENTIFIED WITH caching_sha2_password BY '<password>';`  
`GRANT ALL PRIVILEGES ON *.* TO '<New User>'@'%' WITH GRANT OPTION;`


### Installing a gitlab-runner

Create a volume in docker for gitlab-runner
`docker volume create gitlab-runner-config`

Install and start gitlab-runner inside docker
```
   docker run -d --name gitlab-runner --restart always \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v gitlab-runner-config:/etc/gitlab-runner \
    gitlab/gitlab-runner:latest

```
