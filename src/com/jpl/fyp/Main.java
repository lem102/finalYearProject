package com.jpl.fyp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.List;

import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.compilerComponent.Lexer;
import com.jpl.fyp.compilerComponent.Parser;

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

        Lexer lexer = new Lexer(sourceCode);
        List<Token> tokenList = lexer.output;

        // printTokenList(tokenList);

        Parser parser = new Parser(tokenList);
	}

	private void printTokenList(List<Token> tokenList)
    {
        for (Token token : tokenList)
        {
            System.out.println(token);
        }
	}
}
