package com.jpl.fyp.classLibrary.nodes;

import java.util.Arrays;

import com.jpl.fyp.classLibrary.*;

public class ExpressionParser
{
    public static ExpressionElementNode parse(Token[] tokens) throws JPLException {
        return createElement(tokens, findRootElementIndex(tokens));
	}

	private static ExpressionElementNode createElement(Token[] tokens, int tokenIndex)
        throws JPLException
    {

        Token[] leftSide = Arrays.copyOfRange(tokens, 0, tokenIndex);
        Token[] rightSide = Arrays.copyOfRange(tokens, tokenIndex+1, tokens.length);
        Token token = tokens[tokenIndex];

		switch (token.tokenType)
		{
		    case Add:
		    case Subtract:
		    case Multiply:
		    case Divide:
            case Equal:
            case NotEqual:
            case GreaterThan:
            case LessThan:
            case GreaterThanOrEqualTo:
            case LessThanOrEqualTo:
            case And:
            case Or:
            {
                return new BinaryElementNode(token, rightSide, leftSide);
            }
		    case Integer:
            case Identifier:
            case ClosingParenthesis:
            {
                if (tokens.length == 1)
                {
                    return new ValueElementNode(token);
                }
                else
                {
                    return new FunctionElementNode(tokens);
                }
            }

            // case OpeningParenthesis:   { return createExpressionNode(elements, tokenIndex); }
		    default:
            {
                throw new JPLException("Expression Node : Invalid token for expression. ");
            }
		}
	}


	private static int findRootElementIndex(Token[] tokens) throws JPLException {
        if (containsType(tokens, TokenType.And))
        {
            return findFirstOccuranceOfTypeLocation(tokens, TokenType.And);
        }
        else if (containsType(tokens, TokenType.Or))
        {
            return findFirstOccuranceOfTypeLocation(tokens, TokenType.Or);
        }
        else if (containsComparison(tokens))
		{
		    return findFirstOccuranceOfComparisonLocation(tokens);
		}
        else if (containsType(tokens, TokenType.Subtract))
		{
		    return findFirstOccuranceOfTypeLocation(tokens, TokenType.Subtract);
		}
        else if (containsType(tokens, TokenType.Add))
		{
		    return findFirstOccuranceOfTypeLocation(tokens, TokenType.Add);
		}
        else if (containsType(tokens, TokenType.Multiply))
		{
		    return findFirstOccuranceOfTypeLocation(tokens, TokenType.Multiply);
		}
		else if (containsType(tokens, TokenType.Divide))
		{
		    return findFirstOccuranceOfTypeLocation(tokens, TokenType.Divide);
		}
		else
		{
            return 0;
		}
	}

	private static boolean containsType(Token[] tokens, TokenType divide)
    {
		return findFirstOccuranceOfTypeLocation(tokens, divide) != -1;
	}

    private static boolean elementIsType(Token tokens, TokenType type)
    {
        TokenType[] types = { type };
        return elementIsOfTypes(tokens, types);
    }

    private static int findFirstOccuranceOfTypeLocation(Token[] tokens, TokenType divide)
    {
        for (int i = 0; i < tokens.length; i++)
        {
            if (elementIsType(tokens[i], divide)) { return i; }
        }
        return -1;
	}

	private static boolean containsComparison(Token[] tokens)
    {
        return findFirstOccuranceOfComparisonLocation(tokens) != -1;
	}

	private static int findFirstOccuranceOfComparisonLocation(Token[] tokens)
    {
        for (int i = 0; i < tokens.length; i++)
        {
            if (tokenIsComparison(tokens[i])) { return i; }
        }
        return -1;
	}

	private static boolean tokenIsComparison(Token tokens)
    {
        TokenType [] types = {
            TokenType.Equal,
            TokenType.NotEqual,
            TokenType.GreaterThan,
            TokenType.LessThan,
            TokenType.GreaterThanOrEqualTo,
            TokenType.LessThanOrEqualTo,
        };
        return elementIsOfTypes(tokens, types);
	}

    private static boolean elementIsOfTypes(Token tokens, TokenType[] types)
    {
        for (TokenType type : types)
        {
            if (type == tokens.tokenType) { return true; }
        }
        return false;
    }
}
