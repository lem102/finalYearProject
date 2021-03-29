@echo off

dir /s /B *.java > sources.txt
javac -d bin -cp bin @sources.txt
java -cp bin com.jpl.fyp.Main %*
del sources.txt
rmdir bin /s /q
