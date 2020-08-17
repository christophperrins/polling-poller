# Poller Project
The polling project is a project used to poll other servers to find the performance of them. It uses a Poller to send the requests, a poller orchestrator to tell the pollers who has responsibility over which endpoint monitoring. Finally a frontend to visually see the data

## Other repos in this project:
- [Poller](https://github.com/christophperrins/polling-poller)
- [Poller Orchestrator](https://github.com/christophperrins/polling-orchestrator)
- [Frontend](https://github.com/christophperrins/polling-frontend)

## Poller
The poller has a name. Multiple poller can have the same name but will be treated as a single entity. Useful for having a poller in different locations (developments to come).

### Installation
The project is built using java and maven

To install run the following steps
```sh
sudo apt update
sudo apt install -y openjdk-u-jdk maven
sudo apt install -y mysql-server
```

Next install and run the actual project
```sh
git clone https://github.com/christophperrins/polling-poller
cd polling-poller
```

Configure the file "application.properties" found in src/resources/

**Run the schema.sql inside the mysql server.**

Then package and run the project

```sh
mvn package
 java -jar target/poller-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```





