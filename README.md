kafka Concept


Este proyecto es un concepto para publicar un mensaje con un servicio rest y luego consumir el topico Kafka



install kafka

https://stackoverflow.com/questions/34512287/how-to-automatically-start-kafka-upon-system-startup-in-ubuntu


Start Zookeeper

./bin/zookeeper-server-start.sh config/zookeeper.properties

Create Kafka Super User:

bin/kafka-configs.sh --zookeeper localhost:2181 --alter --add-config 'SCRAM-SHA-512=[password='admin-secret']' --entity-type users --entity-name admin


Create kafka_server_jaas.conf file in config folder

KafkaServer {
org.apache.kafka.common.security.scram.ScramLoginModule required
username="admin"
password="admin-secret";
};

Create ssl-user-config.properties file in config folder

security.protocol=SASL_SSL
sasl.mechanism=SCRAM-SHA-512
sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="demo-user" password="secret";
ssl.truststore.location=/home/debian/store/truststore/kafka.truststore.jks
ssl.truststore.password=kafkakeystore


Append below security properties to existing Kafka server.properties file in config folder.


########### SECURITY using SCRAM-SHA-512 and SSL ###################

listeners=PLAINTEXT://localhost:9092,SASL_PLAINTEXT://localhost:9093,SASL_SSL://localhost:9094
advertised.listeners=PLAINTEXT://localhost:9092,SASL_PLAINTEXT://localhost:9093,SASL_SSL://localhost:9094
security.inter.broker.protocol=SASL_SSL
ssl.endpoint.identification.algorithm=
ssl.client.auth=required
sasl.mechanism.inter.broker.protocol=SCRAM-SHA-512
sasl.enabled.mechanisms=SCRAM-SHA-512

# Broker security settings
ssl.truststore.location=/home/debian/store/truststore/kafka.truststore.jks
ssl.truststore.password=kafkakeystore
ssl.keystore.location=/home/debian/store/keystore/kafka.keystore.jks
ssl.keystore.password=kafkakeystore
ssl.key.password=kafka

# ACLs
authorizer.class.name=kafka.security.auth.SimpleAclAuthorizer
super.users=User:admin

#zookeeper SASL
zookeeper.set.acl=false
########### SECURITY using SCRAM-SHA-512 and SSL ###################



Now Start Kafka with jaas conf file:

export KAFKA_OPTS=-Djava.security.auth.login.config=/home/debian/kafka26/config/kafka_server_jaas.conf

./bin/kafka-server-start.sh ./config/server.properties

on service

sudo vi /etc/systemd/system/kafka.service

Environment="KAFKA_OPTS=-Djava.security.auth.login.config=/home/debian/kafka26/config/kafka_server_jaas.conf"



list acls
./bin/kafka-acls.sh --list --authorizer-properties zookeeper.connect=localhost:2181

ACL de un topico
bin/kafka-acls.sh -authorizer-properties zookeeper.connect=localhost:2181 --list --topic 'test-topic2'
bin/kafka-acls.sh -authorizer-properties zookeeper.connect=localhost:2181 --list --topic 'test-topic2' --resource-pattern-type match

Lista usuarios

./bin/kafka-configs.sh --zookeeper localhost:2181 --describe --entity-type users 




1.- Crear un usuario

 ./bin/kafka-configs.sh --zookeeper localhost:2181 --alter --add-config 'SCRAM-SHA-512=[password='marcelo123']' --entity-type users --entity-name marcelo

2.- Crear topico


./bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --command-config ./config/ssl-user-config.properties --replication-factor 1 --partitions 1 --topic topic-marcelo
 
 
 3.- setear productor
 
 ./bin/kafka-acls.sh --authorizer-properties zookeeper.connect=localhost:2181 --add --allow-principal User:marcelo --producer --topic topic-marcelo
 

4.- setear consumer

./bin/kafka-acls.sh --authorizer-properties zookeeper.connect=localhost:2181 --add --allow-principal User:marcelo --consumer --topic topic-marcelo --group marcelo-consumer-group

