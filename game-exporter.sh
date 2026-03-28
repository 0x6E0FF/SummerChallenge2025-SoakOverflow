#! /bin/bash

mvn test-compile 

if [ $? -eq 0 ]; then
    if [ $# -gt 0 ]; then
        mvn -q exec:java -Dexec.mainClass="GameExporter" -Dexec.classpathScope=test > $1
    else
        mvn exec:java -Dexec.mainClass="GameExporter" -Dexec.classpathScope=test 
    fi 
fi
