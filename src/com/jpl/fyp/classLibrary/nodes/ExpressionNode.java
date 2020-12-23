package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;

public class ExpressionNode extends ExpressionElementNode
{
    public ExpressionElementNode rootExpressionElementNode;

    public ExpressionNode(Token[] tokens) throws JPLException
    {
        ExpressionElementNode[] expressionTokens = convertToExpressionElementNodes(tokens);
        validateExpressionTokens(expressionTokens);
        rootExpressionElementNode = parse(expressionTokens);
    }

    private void validateExpressionTokens(ExpressionElementNode[] expressionTokens)
        throws JPLException
    {
        for (ExpressionElementNode expressionElementNode : expressionTokens)
        {
            System.out.println(expressionElementNode);
        }
        
        if (expressionTokens.length % 2 != 1)
        {
            throw new JPLException("Expression Node : Invalid number of expression tokens.");
        }

        ensureNumberOfComparisonsUnderTwo(expressionTokens);
	}

	private void ensureNumberOfComparisonsUnderTwo(ExpressionElementNode[] expressionTokens)
        throws JPLException
    {
        int comparisonTokensFound = 0;
        for (ExpressionElementNode token : expressionTokens)
        {
            if (tokenIsComparison(token)) { comparisonTokensFound++; }
            if (comparisonTokensFound > 2)
            {
                throw new JPLException("Expression Node : Too many comparison tokens, maximum 1.");
            }
        }
	}

	private boolean tokenIsComparison(ExpressionElementNode token)
    {
		return token instanceof ExpressionComparisonNode;
	}

	private ExpressionElementNode[] convertToExpressionElementNodes(Token[] tokens) throws JPLException
    {
        var output = new ArrayList<ExpressionElementNode>();

        for (int tokenIndex = 0; tokenIndex < tokens.length; tokenIndex++)
        {
            ExpressionElementNode element = createElement(tokens, tokenIndex);
            output.add(element);
        }
        
		return output.toArray(new ExpressionElementNode[output.size()]);
	}

	private ExpressionElementNode createElement(Token[] tokens, int tokenIndex)
			throws JPLException
    {
		switch (tokens[tokenIndex].tokenType)
		{
		    case OpeningParenthesis: { return createExpressionNode(tokens, tokenIndex); }
		    case Integer: { return new IntegerNode(tokens[tokenIndex]); }
		    case Plus: { return new PlusNode(); }
		    case Minus: { return new MinusNode(); }
		    case Multiply: { return new MultiplyNode(); }
		    case Divide: { return new DivideNode(); }
		    case Identifier: { return new VariableNode(tokens[tokenIndex]); }
            case Equal: { return new EqualNode(); }
            case NotEqual: { return new NotEqualNode(); }
            case GreaterThan: { return new GreaterThanNode(); }
            case LessThan: { return new LessThanNode(); }
            case GreaterThanOrEqualTo: { return new GreaterThanOrEqualToNode(); }
            case LessThanOrEqualTo: { return new LessThanOrEqualToNode(); }
		    default: { throw new JPLException("Expression Node : Invalid token for expression. "); }
		}
	}

    private ExpressionElementNode createExpressionNode(Token[] tokens, int tokenIndex)
        throws JPLException
    {
        Token[] bracketedExpression = Arrays.copyOfRange(tokens, tokenIndex, tokens.length);
        int relevantTokenUpperLimit = findClosingParenthesis(bracketedExpression);
        Token[] relevantTokens = Arrays.copyOfRange(tokens, tokenIndex, relevantTokenUpperLimit);
        return new ExpressionNode(relevantTokens).rootExpressionElementNode;
	}

	private int findClosingParenthesis(Token[] tokens)
    {
        var tokenIndex = 0;
        var openingParenthesisPassed = 1;

        while (true)
        {
            if (tokenIndex >= tokens.length
                ||
                openingParenthesisPassed <= 0)
            {
                break;
            }

            switch (tokens[tokenIndex].tokenType)
            {
                case OpeningParenthesis:
                {
                    openingParenthesisPassed++;
                    break;
                }
                case ClosingParenthesis:
                {
                    openingParenthesisPassed--;
                    break;
                }
                default:
                {
                    break;
                }
            }

            tokenIndex++;
        }
		return tokenIndex;
	}

	private ExpressionElementNode parse(ExpressionElementNode[] expressionTokens) throws JPLException
    {
        // should have a binary operator node type,
        // this class can have left and right children which can be expressionElementNodes
        // how can we handle single nodes?

        
        
        throw new JPLException("not implemented lol");
        
		// return null;
	}

	@Override
    public String toString()
    {
        return rootExpressionElementNode.toString();
    }
}
