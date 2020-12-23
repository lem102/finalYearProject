package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;

public class ExpressionNode extends ExpressionElementNode
{
    public ExpressionElementNode rootExpressionElementNode;

    public ExpressionNode(Token[] elements) throws JPLException
    {
        ExpressionElementNode[] expressionElements = convertToExpressionElementNodes(elements);
        validateExpressionElements(expressionElements);
        rootExpressionElementNode = parse(expressionElements);
    }

    private void validateExpressionElements(ExpressionElementNode[] expressionElements)
        throws JPLException
    {
        for (ExpressionElementNode expressionElementNode : expressionElements)
        {
            System.out.println(expressionElementNode);
        }
        
        if (expressionElements.length % 2 != 1)
        {
            throw new JPLException("Expression Node : Invalid number of expression elements.");
        }

        ensureNumberOfComparisonsUnderTwo(expressionElements);
	}

    private ExpressionElementNode parse(ExpressionElementNode[] expressionElements) throws JPLException
    {
        // should have a binary operator node type,
        // this class can have left and right children which can be expressionElementNodes
        // how can we handle single nodes?

        if (containsComparison(expressionElements))
        {
            int comparisonLocation = findComparisonLocation(expressionElements);
        }

        throw new JPLException("not implemented lol");
        
		// return null;
	}

	private boolean containsComparison(ExpressionElementNode[] expressionElements)
    {
        return findComparisonLocation(expressionElements) != -1;
	}

	private int findComparisonLocation(ExpressionElementNode[] expressionElements)
    {
        for (int i = 0; i < expressionElements.length; i++)
        {
            if (expressionElements[i] instanceof BinaryComparisonNode) { return i; }
        }
        return -1;
	}

	private void ensureNumberOfComparisonsUnderTwo(ExpressionElementNode[] expressionElements)
        throws JPLException
    {
        int comparisonElementsFound = 0;
        for (ExpressionElementNode token : expressionElements)
        {
            if (tokenIsComparison(token)) { comparisonElementsFound++; }
            if (comparisonElementsFound > 2)
            {
                throw new JPLException("Expression Node : Too many comparison elements, maximum 1.");
            }
        }
	}

	private boolean tokenIsComparison(ExpressionElementNode token)
    {
		return token instanceof ExpressionComparisonNode;
	}

	private ExpressionElementNode[] convertToExpressionElementNodes(Token[] elements) throws JPLException
    {
        var output = new ArrayList<ExpressionElementNode>();

        for (int tokenIndex = 0; tokenIndex < elements.length; tokenIndex++)
        {
            ExpressionElementNode element = createElement(elements, tokenIndex);
            output.add(element);
        }
        
		return output.toArray(new ExpressionElementNode[output.size()]);
	}

	private ExpressionElementNode createElement(Token[] elements, int tokenIndex)
			throws JPLException
    {
		switch (elements[tokenIndex].tokenType)
		{
		    case OpeningParenthesis:   { return createExpressionNode(elements, tokenIndex); }
		    case Integer:              { return new IntegerNode(elements[tokenIndex]); }
		    case Add:                  { return new BinaryOperatorNode(JPLBinaryOperator.Add); }
		    case Subtract:             { return new BinaryOperatorNode(JPLBinaryOperator.Subtract); }
		    case Multiply:             { return new BinaryOperatorNode(JPLBinaryOperator.Multiply); }
		    case Divide:               { return new BinaryOperatorNode(JPLBinaryOperator.Divide); }
		    case Identifier:           { return new VariableNode(elements[tokenIndex]); }
            case Equal:                { return new BinaryComparisonNode(JPLBinaryComparison.Equal); }
            case NotEqual:             { return new BinaryComparisonNode(JPLBinaryComparison.NotEqual); }
            case GreaterThan:          { return new BinaryComparisonNode(JPLBinaryComparison.GreaterThan); }
            case LessThan:             { return new BinaryComparisonNode(JPLBinaryComparison.LessThan); }
            case GreaterThanOrEqualTo: { return new BinaryComparisonNode(JPLBinaryComparison.GreaterThanOrEqualTo); }
            case LessThanOrEqualTo:    { return new BinaryComparisonNode(JPLBinaryComparison.LessThanOrEqualTo); }
		    default:                   { throw new JPLException("Expression Node : Invalid token for expression. "); }
		}
	}

    private ExpressionElementNode createExpressionNode(Token[] elements, int tokenIndex)
        throws JPLException
    {
        Token[] bracketedExpression = Arrays.copyOfRange(elements, tokenIndex, elements.length);
        int relevantTokenUpperLimit = findClosingParenthesis(bracketedExpression);
        Token[] relevantElements = Arrays.copyOfRange(elements, tokenIndex, relevantTokenUpperLimit);
        return new ExpressionNode(relevantElements).rootExpressionElementNode;
	}

	private int findClosingParenthesis(Token[] elements)
    {
        var tokenIndex = 0;
        var openingParenthesisPassed = 1;

        while (true)
        {
            if (tokenIndex >= elements.length
                ||
                openingParenthesisPassed <= 0)
            {
                break;
            }

            switch (elements[tokenIndex].tokenType)
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

	@Override
    public String toString()
    {
        return rootExpressionElementNode.toString();
    }
}
