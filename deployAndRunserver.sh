#! /bin/bash
cd Workspace
mvn clean install
cd server
docker-compose up
