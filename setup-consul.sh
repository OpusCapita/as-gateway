#!/bin/sh

putConsulData() {
    curl -X PUT -d ${MYSQL_DATABASE} http://consul:8500/v1/kv/gateway/as/db-init/database &&
    curl -X PUT -d ${MYSQL_ROOT_USER} http://consul:8500/v1/kv/gateway/as/db-init/user &&
    curl -X PUT -d ${MYSQL_ROOT_PASSWORD} http://consul:8500/v1/kv/gateway/as/db-init/password &&
    curl -X PUT -d ${KAFKA_USER} http://consul:8500/v1/kv/gateway/as/kafka/user &&
    curl -X PUT -d ${KAFKA_PASSWORD} http://consul:8500/v1/kv/gateway/as/kafka/password
}

putConsulData

while [ $? -ne 0 ]; do
    sleep 1
    echo "Could not connect to consul. Retrying..."
    putConsulData
done