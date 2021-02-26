package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;

public class ExpressionElementNode
{
    private Token token;

    public ExpressionElementNode(Token token) {
        this.token = token;
    }

	public Token getToken() {
		return token;
	}

	public void validate(SymbolTableEntry[] entries) throws JPLException {
        
	}

	public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        System.out.println("Jacob Warning : This expression element node has not been programmed.");
		return null;
	}
}

