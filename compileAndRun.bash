#!/bin/bash

if javac -d bin -cp bin $(find . -name "*.java")
then
    java -cp bin com.jpl.fyp.Main $1
fi
