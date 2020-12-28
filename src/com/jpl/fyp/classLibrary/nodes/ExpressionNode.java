package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class ExpressionNode
{
    public ExpressionElementNode rootExpressionElementNode;

    public ExpressionNode(Token[] tokens) throws JPLException
    {
        // creation of expression element nodes should occur during the parse method.
        validateTokens(tokens);
        rootExpressionElementNode = ExpressionParser.parse(tokens);
    }

    private void validateTokens(Token[] tokens)
        throws JPLException
    {
        if (tokens.length % 2 != 1)
        {
            // throw new JPLException("Expression Node : Invalid number of expression elements.");
        }

        ensureNumberOfComparisonsUnderTwo(tokens);
	}

	private void ensureNumberOfComparisonsUnderTwo(Token[] tokens)
        throws JPLException
    {
        int comparisonElementsFound = 0;
        for (Token token : tokens)
        {
            if (tokenIsComparison(token)) { comparisonElementsFound++; }
            if (comparisonElementsFound > 2)
            {
                throw new JPLException("Expression Node : Too many comparison elements, maximum 1.");
            }
        }
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

	@Override
    public String toString()
    {
        return rootExpressionElementNode.toString();
    }
}
