package com.jpl.fyp.classLibrary;

public class Token
{
    public TokenType tokenType;

    public String tokenValue;

    public Token(TokenType type, String actualValue)
    {
        tokenType = type;
        tokenValue = actualValue;
    }

    public String toString()
    {
        return String.format("Token: {%-20s %-10s}", tokenType, tokenValue);
    }
}
