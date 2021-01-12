package com.jpl.fyp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;

import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.nodes.RootNode;
import com.jpl.fyp.compilerComponent.*;

public class Main
{
    public static void main(String[] args) throws Exception 
    {
        if (args.length != 1)
        {
            throw new InvalidParameterException("A source file must be provided.");
        }

        Main main = new Main();
        main.start(args[0]);
    }

    private void start(String filePath) throws Exception
    {
        String sourceCode = Files.readString(Path.of(filePath));
        Token[] tokens = Lexer.convertSourceCodeToTokens(sourceCode);
        RootNode syntaxTree = Parser.parse(tokens);
        System.out.println(syntaxTree);
        // var intermediateCode = new IntermediateCodeGenerator();
	}

	private void printTokenList(Token[] tokens)
    {
        for (Token token : tokens)
        {
            System.out.println(token);
        }
	}
}
