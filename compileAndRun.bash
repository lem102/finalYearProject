#!/bin/bash

# compile
javac -d bin -cp bin $(find . -name "*.java")

# run
java -cp bin com.jpl.fyp.Main $1
