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

cd into the directory `capacitor-runner-docker/` and compile the docker file for the gitlab runner using  
`sudo docker build -t capacitor-gitlab-runner ./`  


Create a volume in docker for gitlab-runner  
`sudo docker volume create gitlab-runner-config`  


Install and start the runner inside docker  
```
   sudo docker run -d --name gitlab-runner --restart always \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v gitlab-runner-config:/etc/gitlab-runner \
    capacitor-gitlab-runner:latest

```  


Register the runner  
`sudo docker run --rm -it -v gitlab-runner-config:/etc/gitlab-runner capacitor-gitlab-runner:latest register`  

Follow the onscreen prompts:
- Enter the domain of your gitlab (https://gitlab.com/ if you are not hosing your own repository)  
- It will ask for a "gitlab-ci token".  Find this in your gitlab repository in the left bar under Settings > CI/CD > Runners.  Hit expand, and it is under the heading "Set up a specific Runner manually".  
- Enter a description  
- Enter any tags  
- Select shell  




