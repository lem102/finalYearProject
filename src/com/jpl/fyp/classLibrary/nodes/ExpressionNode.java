package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;

import com.jpl.fyp.classLibrary.nodes.ExpressionElementNode;
import com.jpl.fyp.classLibrary.Token;

public class ExpressionNode
{
    public ExpressionElementNode rootExpressionElementNode;

    public ExpressionNode(Token[] tokens)
    {
        ExpressionElementNode[] expressionTokens = convertToExpressionElementNodes(tokens);
        this.rootExpressionElementNode = parse(expressionTokens);
    }

    private ExpressionElementNode[] convertToExpressionElementNodes(Token[] tokens)
    {
        // Focus on implementing this sucker
        var output = new ArrayList<ExpressionElementNode>();
        var tokenIndex = 0;


        ExpressionElementNode element;
        
        switch (tokens[tokenIndex].tokenType)
        {
            case Integer:
            {
                element = new IntegerNode(tokens[tokenIndex]);
                break;
            }
            case Plus:
            {
                element = new PlusNode();
                break;
            }
            case Minus:
            {
                element = new MinusNode();
                break;
            }
            case Multiply:
            {
                element = new MultiplyNode();
                break;
            }
            case Divide:
            {
                element = new DivideNode();
                break;
            }
            case Identifier:
            {
                // be careful here, you need to figure out how the syntax table is going to work before working on this section.
                element = new VariableNode();
                break;
            }
            default:
            {
                break;
            }
        }
        
        
		return output.toArray(new ExpressionElementNode[output.size()]);
	}

	private ExpressionElementNode parse(ExpressionElementNode[] expressionTokens)
    {
		return null;
	}

	@Override
    public String toString()
    {
        return rootExpressionElementNode.toString();
    }
}
