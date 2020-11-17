# The Capacitor Archive setup

## Setting up a VM for CI/CD and MySQL using docker containers

### Install docker and docker-compose
Inside a linux machine, (either a VM or bare metal) install docker    
`sudo apt-get update`  
`sudo apt install docker.io`  

Start and enable docker  
`sudo systemctl start docker`  
`sudo systemctl enable docker`  

Install Docker Compose  
`sudo apt install docker-compose`  

### Set mysql credentials
Store the credentials for mysql in a file mysql-variables.env in the root directory of the project.  
```
CAP_DB_ROOT_PASSWORD=<root-pass>
CAP_DB_DBA_USERNAME=<dba-username>
CAP_DB_DBA_PASSWORD=<dba-password>
```

### Create docker containers for mysql, phpmyadmin and gitlab-runner
Run the following command which executes docker-compose.yml  
`sudo docker-compose up -d`  


### Register the runner  
This should only have to be done once after running docker-compose for the first time.  
Open a shell inside the container.  
`sudo docker exec -it gitlab-runner bash`  
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
Phpmyadmin can be accessed from http://<Host-Address>:8081/.  (The server address of the database is <Host-Address>:3306)  
Any container can be accessed through a shell using  
`sudo docker exec -it <container-name> bash`
