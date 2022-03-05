# Tasker-DropWizard

## Prerequisites

1. Make sure, you have maven installed on the machine where you want to build and run this application (refer: https://maven.apache.org/install.html)
2. Minimum Open Jdk 11 is needed
3. Ensure ports 9000 and 9001 are reserved for this application otherwise you can of course change the ports in tasker-dw/tasker.yml
4. To run build a docker image, docker must be installed. This is optional if you are directly running this application as Java application from IDE or command.
5. MySQL 8.0.28 server must be running. You can run "docker-compose up -d tasker-db" if you prefer mysql docker image.
    5.1. create below table before using the tasker APIs
        refer tasker-dw/src/resources/database_scripts.sql

How to start the tasker application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/tasker-dw-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localdocker:9000`

Below ar the defined APIs
1. DELETE  /tasker?taskId=`<taskId>`
2. GET     /tasker?taskId=`<taskId>`
3. POST    /tasker/create
4. GET     /tasker/findAll

Health Check
---

To see your applications health enter url `http://localdocker:9001/healthcheck`


How to run as Docker container?
---

1. After checking the the code to your machine, run "mvn clean package" under the package folder
2. Run "sh buildAndRun.sh"
3. run command "docker-compose up -d or docker-compose up -d tasker-db tasker-dw"

Note: Assume you must have finished all prerequisites before these steps.
