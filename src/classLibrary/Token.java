package src.classLibrary;

public class Token
{
    private TokenType tokenType;

    private String tokenValue;

    public Token(TokenType type, String actualValue)
    {
        tokenType = type;
        tokenValue = actualValue;
    }

    public TokenType getTokenType()
    {
        return tokenType;
    }

    public String getTokenValue()
    {
        return tokenValue;
    }
}
