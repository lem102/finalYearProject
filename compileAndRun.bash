#!/bin/bash

if javac -g -d bin -cp bin $(find . -name "*.java")
then
    if java -cp bin com.jpl.fyp.Main $1 $2
    then
        nasm -f elf output.asm
        ld -m elf_i386 output.o -o output
        ./output
    fi
    rm -rf bin/*
fi
