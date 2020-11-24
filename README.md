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
Store the credentials for mysql in a file `mysql-variables.env` in the root directory of the project.  
```
MYSQL_ROOT_PASSWORD=<root-pass>
MYSQL_USER=<dba-username>
MYSQL_PASSWORD=<dba-password>
```

### Create docker containers for mysql, phpmyadmin and gitlab-runner
Run the following command which executes docker-compose.yml  
`sudo docker-compose build`
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
Phpmyadmin can be accessed from `http://<Host-Address>:8081/`.  (The server address of the database is `<Host-Address>:3306`)  
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




