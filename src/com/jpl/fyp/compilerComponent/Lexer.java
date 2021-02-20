package com.jpl.fyp.compilerComponent;

import java.util.ArrayList;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class Lexer
{
    public static Token[] convertSourceCodeToTokens(String sourceCode) throws JPLException {
        String[] splitSourceCode = splitSourceIntoTokens(sourceCode);
        return convertStringsToTokens(splitSourceCode);
    }

	private static String[] splitSourceIntoTokens(String sourceCode) {
        sourceCode = sourceCode.replaceAll("\\s+", " ");
        var tokens = new ArrayList<String>();
        var currentToken = "";
        
        for (int i = 0; i < sourceCode.length(); i++) {
            Character currentChar = sourceCode.charAt(i);

            if ((Character.isWhitespace(currentChar) || isCharSymbolOrPunctuation(currentChar)) && currentToken != "") {
                tokens.add(currentToken);
                currentToken = "";
            }
            
            if (Character.isLetterOrDigit(currentChar)) {
                currentToken += currentChar;
            } else if (isCharSymbolOrPunctuation(currentChar)) {
                Character previousChar = sourceCode.charAt(i-1);
                if (isCombinationRequired(previousChar, currentChar)) {
                    tokens.set(tokens.size() - 1, previousChar.toString() + currentChar.toString());                    
                } else {
                    tokens.add(currentChar.toString());
                }
            }
        }
        
		return tokens.toArray(new String[tokens.size()]);
	}

	private static boolean isCharSymbolOrPunctuation(char currentChar)
    {
        int characterType = Character.getType(currentChar);
        return characterType >= 20 && characterType <= 25;
    }

	private static boolean isCombinationRequired(Character previousCharacter, Character currentCharacter) {
        return (currentCharacter == '=' && (previousCharacter == '=' || previousCharacter == '!'))
            || (currentCharacter == '|' && previousCharacter == '|')
            || (currentCharacter == '&' && previousCharacter == '&');
	}

	private static Token[] convertStringsToTokens(String[] splitSourceCode) throws JPLException {
        var tokenList = new ArrayList<Token>();
        
        for (String tokenString : splitSourceCode) {
            tokenList.add(createToken(tokenString));
        }

		return tokenList.toArray(new Token[tokenList.size()]);
	}

	private static Token createToken(String tokenString) throws JPLException {
        TokenType tokenType;

        switch (tokenString) {
            case "while": {
                tokenType = TokenType.While;
                break;
            }
            case "define": {
                tokenType = TokenType.Define;
                break;
            }
            case "if": {
                tokenType = TokenType.If;
                break;
            }
            case "else": {
                tokenType = TokenType.Else;
                break;
            }
            case "int": {
                tokenType = TokenType.IntegerDeclaration;
                break;
            }
            case "(": {
                tokenType = TokenType.OpeningParenthesis;
                break;
            }
            case ")": {
                tokenType = TokenType.ClosingParenthesis;
                break;
            }
            case ",": {
                tokenType = TokenType.Comma;
                break;
            }
            case "{": {
                tokenType = TokenType.OpeningBrace;
                break;
            }
            case "}": {
                tokenType = TokenType.ClosingBrace;
                break;
            }
            case "=": {
                tokenType = TokenType.Assignment;
                break;
            }
            case "+": {
                tokenType = TokenType.Add;
                break;
            }
            case "-": {
                tokenType = TokenType.Subtract;
                break;
            }
            case "*": {
                tokenType = TokenType.Multiply;
                break;
            }
            case "/": {
                tokenType = TokenType.Divide;
                break;
            }
            case ";": {
                tokenType = TokenType.Semicolon;
                break;
            }
            case "==": {
                tokenType = TokenType.Equal;
                break;
            }
            case "||": {
                tokenType = TokenType.Or;
                break;
            }
            case "&&": {
                tokenType = TokenType.And;
                break;
            }
            case "!=": {
                tokenType = TokenType.NotEqual;
                break;
            }
            default: {
                if (isStringNumeric(tokenString)) {
                    tokenType = TokenType.Integer;
                }
                else if (isStringAlpha(tokenString)) {
                    tokenType = TokenType.Identifier;
                }
                else {
                    throw new JPLException("Unrecognised token: " + tokenString);
                }
                break;
            }
        }
        
		return new Token(tokenType, tokenString);
	}

	private static boolean isStringAlpha(String string) {
        return string.matches("[a-zA-Z]+");
	}

	private static boolean isStringNumeric(String string) {
        if (string == null) {
            return false;
        }

        try {
            Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            return false;
        }
		return true;
	}
}
