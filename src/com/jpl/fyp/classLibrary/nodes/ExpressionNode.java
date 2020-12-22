package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;

import com.jpl.fyp.classLibrary.ExpressionElement;
import com.jpl.fyp.classLibrary.Token;

public class ExpressionNode
{
    public ExpressionElement rootExpressionElement;

    public ExpressionNode(Token[] tokens)
    {
        ExpressionElement[] expressionTokens = convertToExpressionElements(tokens);
        this.rootExpressionElement = parse(expressionTokens);
    }

    private ExpressionElement[] convertToExpressionElements(Token[] tokens)
    {
        // Focus on implementing this sucker
        var output = new ArrayList<ExpressionElement>();
        var tokenIndex = 0;


        ExpressionElement element = new ExpressionElement();
        
        switch (tokens[tokenIndex].tokenType)
        {
            case Integer:
            {
                
                break;
            }
            default:
            {
                break;
            }
        }
        
        
		return output.toArray(new ExpressionElement[output.size()]);
	}

	private ExpressionElement parse(ExpressionElement[] expressionTokens)
    {
		return null;
	}

	@Override
    public String toString()
    {
        return rootExpressionElement.toString();
    }
}
