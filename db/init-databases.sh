#!/bin/sh
set -e

psql --username postgres --dbname postgres <<-EOSQL
  CREATE DATABASE microservices;
  CREATE ROLE microservices WITH ENCRYPTED PASSWORD 'microservices' LOGIN;
  GRANT ALL PRIVILEGES ON DATABASE microservices TO microservices;
EOSQL

psql --username postgres --dbname microservices <<-EOSQL
  GRANT ALL ON SCHEMA public TO microservices;
EOSQL
