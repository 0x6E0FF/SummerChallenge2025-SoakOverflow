#! /bin/bash

mvn test-compile &&\
mvn exec:java -Dexec.mainClass="GameExporter" -Dexec.classpathScope=test 
