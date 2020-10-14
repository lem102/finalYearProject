import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        if (args.length != 1)
        {
            throw new InvalidParameterException("A source file must be provided.");
        }

        Main main = new Main();
        String sourceCode = main.readFile(args[0]);
        System.out.println(sourceCode);
    }

    private String readFile(String fileName) throws FileNotFoundException, IOException
    {
        File file = new File(fileName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String sourceCode;
        
        while ((sourceCode = bufferedReader.readLine()) != null)
        {
            System.out.println(sourceCode);
        }

        bufferedReader.close();

        return sourceCode;
    }
}