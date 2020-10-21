package com.jpl.fyp.classLibrary;

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

    public String toString()
    {
        return String.format("Token: {%-20s %-10s}", tokenType, tokenValue);
    }
}
