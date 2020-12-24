package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.TokenType;

public class ExpressionElementNode
{
    private TokenType type;

    public ExpressionElementNode(TokenType tokenType)
    {
        this.type = tokenType;
    }

	public TokenType getType()
    {
		return type;
	}
}

