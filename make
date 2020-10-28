#!/bin/sh
mvn clean install
cp target/imaging-server-0.0.1-SNAPSHOT-jar-with-dependencies.jar ./imaging-server.jar
