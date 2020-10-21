package com.jpl.fyp.compilerComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class Lexer
{
    public List<Token> output;
    
    public Lexer(String sourceCode) throws Exception
    {
        List<String> tokens = splitSourceIntoTokens(sourceCode);
        output = convertStringsToTokens(tokens);
    }

	private List<Token> convertStringsToTokens(List<String> tokens) throws Exception
    {
        ArrayList<Token> tokenList = new ArrayList<Token>();
        
        for (String tokenString : tokens)
        {
            tokenList.add(createToken(tokenString));
        }
        
		return tokenList;
	}

	private Token createToken(String tokenString) throws Exception
    {
        TokenType tokenType;

        switch (tokenString)
        {
            case "while":
            {
                tokenType = TokenType.While;
                break;
            }
            case "define":
            {
                tokenType = TokenType.Define;
                break;
            }
            case "if":
            {
                tokenType = TokenType.If;
                break;
            }
            case "else":
            {
                tokenType = TokenType.Else;
                break;
            }
            case "int":
            {
                tokenType = TokenType.IntegerDeclaration;
                break;
            }
            case "(":
            {
                tokenType = TokenType.OpeningParenthesis;
                break;
            }
            case ")":
            {
                tokenType = TokenType.ClosingParenthesis;
                break;
            }
            case ",":
            {
                tokenType = TokenType.Comma;
                break;
            }
            case "{":
            {
                tokenType = TokenType.OpeningBrace;
                break;
            }
            case "}":
            {
                tokenType = TokenType.ClosingBrace;
                break;
            }
            case "=":
            {
                tokenType = TokenType.Assignment;
                break;
            }
            case ";":
            {
                tokenType = TokenType.Semicolon;
                break;
            }
            case "==":
            {
                tokenType = TokenType.Equal;
                break;
            }
            case "||":
            {
                tokenType = TokenType.Or;
                break;
            }
            case "&&":
            {
                tokenType = TokenType.And;
                break;
            }
            case "!=":
            {
                tokenType = TokenType.NotEqual;
                break;
            }
            default:
            {
                if (isStringNumeric(tokenString))
                {
                    tokenType = TokenType.Integer;
                }
                else if (isStringAlpha(tokenString))
                {
                    tokenType = TokenType.Identifier;
                }
                else
                {
                    throw new Exception("Unrecognised token: " + tokenString);
                }
                break;
            }
        }
        
		return new Token(tokenType, tokenString);
	}

	private boolean isStringAlpha(String string)
    {
        return string.matches("[a-zA-Z]+");
	}

	private boolean isStringNumeric(String string)
    {
        if (string == null)
        {
            return false;
        }

        try
        {
            Integer.parseInt(string);
        }
        catch (NumberFormatException exception)
        {
            return false;
        }

		return true;
	}

	private List<String> splitSourceIntoTokens(String sourceCode)
    {
        List<String> tokens = new ArrayList<String>();
        sourceCode = sourceCode.replaceAll("\\s+", " ");

        String currentToken = "";

        for (int i = 0; i < sourceCode.length(); i++)
        {
            Character currentChar = sourceCode.charAt(i);

            if (Character.isLetterOrDigit(currentChar))
            {
                currentToken += currentChar;
            }
            else if (isCharSymbolOrPunctuation(currentChar))
            {
                Character previousChar = sourceCode.charAt(i-1);
                currentToken = handleSymbolOrPunctuation(tokens,
                                                         currentToken,
                                                         currentChar,
                                                         previousChar);
            }
            else if (Character.isWhitespace(currentChar))
            {
                if (currentToken != "")
                {
                    tokens.add(currentToken);
                    currentToken = "";
                }
            }
        }
        
		return tokens;
	}

	private String handleSymbolOrPunctuation(List<String> tokens,
                                             String currentToken,
                                             Character currentChar,
                                             Character previousChar)
    {
        if (currentToken != "")
        {
            tokens.add(currentToken);
            currentToken = "";
        }

        if (currentChar == '=')
        {
            tokens = handleSymbol('=',
                                  new ArrayList<Character>(Arrays.asList('=', '!')),
                                  tokens,
                                  previousChar);
        }
        else if (currentChar == '|')
        {
            tokens = handleSymbol('|',
                                  new ArrayList<Character>(Arrays.asList('|')),
                                  tokens,
                                  previousChar);
        }
        else if (currentChar == '&')
        {
            tokens = handleSymbol('&',
                                  new ArrayList<Character>(Arrays.asList('&')),
                                  tokens,
                                  previousChar);
        }
        else
        {
            tokens.add(currentChar.toString());
        }

        
		return currentToken;
	}

	private List<String> handleSymbol(Character currentChar,
                                      ArrayList<Character> combineChars,
                                      List<String> tokens,
                                      Character previousChar)
    {
        boolean changed = false;
        
        for (Character character : combineChars)
        {
            if (previousChar == character)
            {
                tokens.remove(tokens.size() - 1);
                tokens.add(character.toString() + currentChar.toString());
                changed = true;
            }
        }

        if (!changed)
        {
            tokens.add(currentChar.toString());
        }
        
		return tokens;
	}

	private boolean isCharSymbolOrPunctuation(char currentChar)
    {
        if (Character.getType(currentChar) == 24
            ||
            Character.getType(currentChar) == 25)
        {
            return true;
        }
        else
        {
            return false;
        }
	}
}
