package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;

public class ExpressionElementNode
{
    private Token token;

    public ExpressionElementNode(Token token)
    {
        this.token = token;
    }

	public Token getToken()
    {
		return token;
	}

	public void validate(SymbolTableEntry[] entries) throws JPLException {
        
	}
}

