package com.jpl.fyp.classLibrary.nodes;

import java.util.Arrays;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class ExpressionParser
{

    public static ExpressionElementNode parse(Token[] tokens) throws JPLException
    {
        // should have a binary operator node type,
        // this class can have left and right children which can be expressionElementNodes
        // how can we handle single nodes?
        
        int rootElementIndex;

        ExpressionElementNode output;

        if (tokens.length == 1)
        {
            rootElementIndex = 0;
            output = createElement(tokens, 0);
        }
        else
        {
            if (containsComparison(tokens))
            {
                rootElementIndex = findFirstOccuranceOfComparisonLocation(tokens);
            }
            else if (containsType(tokens, TokenType.Divide))
            {
                rootElementIndex = findFirstOccuranceOfTypeLocation(tokens, TokenType.Divide);
            }
            else if (containsType(tokens, TokenType.Multiply))
            {
                rootElementIndex = findFirstOccuranceOfTypeLocation(tokens, TokenType.Multiply);
            }
            else if (containsType(tokens, TokenType.Add))
            {
                rootElementIndex = findFirstOccuranceOfTypeLocation(tokens, TokenType.Add);
            }
            else if (containsType(tokens, TokenType.Subtract))
            {
                rootElementIndex = findFirstOccuranceOfTypeLocation(tokens, TokenType.Subtract);
            }
            else
            {
                throw new JPLException("Expression Parser : panic");
            }
            Token[] leftSide = Arrays.copyOfRange(tokens, 0, rootElementIndex);
            Token[] rightSide = Arrays.copyOfRange(tokens, rootElementIndex+1, tokens.length);
            output = createElement(tokens, rootElementIndex, leftSide, rightSide);
        }



        return output;
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

    private static ExpressionElementNode createElement(Token[] tokens, int i) throws JPLException
    {
		return createElement(tokens, i, new Token[0], new Token[0]);
	}

	private static ExpressionElementNode createElement(Token[] tokens, int tokenIndex, Token[] leftSide, Token[] rightSide)
        throws JPLException
    {
        var tokenType = tokens[tokenIndex].tokenType;
		switch (tokenType)
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
            {
                return new BinaryElementNode(tokenType, rightSide, leftSide);
            }
		    case Integer:              { return new IntegerNode(TokenType.Integer, tokens[tokenIndex]); }
            case Identifier:           { return new VariableNode(TokenType.Identifier, tokens[tokenIndex]); }
                // case OpeningParenthesis:   { return createExpressionNode(elements, tokenIndex); }
		    default:                   { throw new JPLException("Expression Node : Invalid token for expression. "); }
		}
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
