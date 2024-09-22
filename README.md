# Order Management
## Project Overview
This project consists two microservices, **OrderService** and **InventoryService** for manage orders and inventory. These microservices connect via Kafka order creation and inventory updates. 

## Requirements
- Java 17
- Maven
- Spring boot
- Kafka

## Dependencies  
- spring boot web
- spring boot JPA 
- spring boot H2 database
- spring boot test
- mokito // for test code

## Install Kafka in local mechine in linux

### create user for kafka
```
sudo adduser kafka
```
```
sudo adduser kafka sudo // add sudo permission
```
### Download kafka from repository and extract 

### Congigure kafka server 

/etc/systemd/system/zookeeper.service

```
[Unit]
Requires=network.target remote-fs.target
After=network.target remote-fs.target

[Service]
Type=simple
User=kafka
ExecStart=/home/kafka/kafka/bin/zookeeper-server-start.sh /home/kafka/kafka/config/zookeeper.properties
ExecStop=/home/kafka/kafka/bin/zookeeper-server-stop.sh
Restart=on-abnormal

[Install]
WantedBy=multi-user.target
```
/etc/systemd/system/kafka.service
```
[Unit]
Requires=zookeeper.service
After=zookeeper.service

[Service]
Type=simple
User=kafka
ExecStart=/bin/sh -c '/home/kafka/kafka/bin/kafka-server-start.sh /home/kafka/kafka/config/server.properties > /home/kafka/kafka/kafka.log 2>&1'
ExecStop=/home/kafka/kafka/bin/kafka-server-stop.sh
Restart=on-abnormal

[Install]
WantedBy=multi-user.target
```
### Run the kafka server 
```
sudo systemctl start kafka
```
```
sudo systemctl enable zookeeper
sudo systemctl enable kafka
```

## Build the maven for both microservices

```
mvn clean install
```
## Run the microservice 

```
cd orderService
mvn spring-boot:run
```
```
cd inventoryService
mvn spring-boot:run
```
## API Endpoints of both microservices

### OrderService 
- POST /orders : Create a new order
- GET /orders/{id} : Retrieve order details by ID.

### InventoryService 
- GET /inventory/{productId} : Retrieve inventory details by product ID.

### Testing 

- Unit test
- Integration test
- kafka event test
