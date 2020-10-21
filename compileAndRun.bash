#!/bin/bash

# compile
javac -d bin -cp bin $(find . -name "*.java")

# javac -d bin -cp bin src/com/jpl/fyp/classLibrary/TokenType.java
# javac -d bin -cp bin src/com/jpl/fyp/classLibrary/Token.java
# javac -d bin -cp bin src/com/jpl/fyp/compilerComponent/Lexer.java
# javac -d bin -cp bin src/com/jpl/fyp/Main.java

# run
java -cp bin com.jpl.fyp.Main $1
