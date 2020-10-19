import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;

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
        String sourceCode = readFile(filePath);
        System.out.println(sourceCode);
	}

	private String readFile(String filePath) throws FileNotFoundException, IOException
    {
        File file = new File(filePath);
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
