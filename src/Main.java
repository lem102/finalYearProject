package src;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.List;

import src.compilerComponent.Lexer;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException, IOException 
    {
        if (args.length != 1)
        {
            throw new InvalidParameterException("A source file must be provided.");
        }

        Main main = new Main();
        main.start(args[0]);
    }

    private void start(String filePath) throws FileNotFoundException, IOException
    {
        String sourceCode = Files.readString(Path.of(filePath));
        System.out.println(sourceCode);

        Lexer lexer = new Lexer(sourceCode);
        List<Token> tokenList = lexer.output;

        // printTokenList(tokenList);

        // Parser parser = new Parser(tokenList);
	}
}
