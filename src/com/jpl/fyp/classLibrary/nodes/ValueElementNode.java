package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.Token;

public class ValueElementNode extends ExpressionElementNode {
    public ValueElementNode(Token token)
    {
        super(token);
    }

	@Override
	public String toString() {
		return "Value Element Node: " + getToken().tokenType + ", " + getToken().tokenValue;
	}
}
