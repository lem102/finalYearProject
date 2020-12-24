#!/bin/bash

if javac -g -d bin -cp bin $(find . -name "*.java")
then
    java -cp bin com.jpl.fyp.Main $1
    rm -rf bin/*
fi
