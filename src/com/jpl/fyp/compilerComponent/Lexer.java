package com.jpl.fyp.compilerComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class Lexer
{
    public Token[] output;
    
    public Lexer(String sourceCode) throws Exception
    {
        String[] splitSourceCode = splitSourceIntoTokens(sourceCode);
        this.output = convertStringsToTokens(splitSourceCode);
    }

	private Token[] convertStringsToTokens(String[] splitSourceCode) throws Exception
    {
        var tokenList = new ArrayList<Token>();
        
        for (String tokenString : splitSourceCode)
        {
            tokenList.add(createToken(tokenString));
        }

		return tokenList.toArray(new Token[tokenList.size()]);
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
            case "+":
            {
                tokenType = TokenType.Add;
                break;
            }
            case "-":
            {
                tokenType = TokenType.Subtract;
                break;
            }
            case "*":
            {
                tokenType = TokenType.Multiply;
                break;
            }
            case "/":
            {
                tokenType = TokenType.Divide;
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

	private String[] splitSourceIntoTokens(String sourceCode)
    {
        var tokens = new ArrayList<String>();
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
        
		return tokens.toArray(new String[tokens.size()]);
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
        int characterType = Character.getType(currentChar);
        return characterType >= 20 && characterType <= 25;
    }
}
