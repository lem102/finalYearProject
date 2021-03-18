#!/bin/bash

if javac -g -d bin -cp bin $(find . -name "*.java")
then
    if java -cp bin com.jpl.fyp.Main $1 $2
    then
        if nasm -f elf output.asm
        then
            if ld -m elf_i386 output.o -o output
            then
                ./output
            fi
        fi
    fi
    rm -rf bin/*
fi
