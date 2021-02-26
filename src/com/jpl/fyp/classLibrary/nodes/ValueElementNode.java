package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.compilerComponent.Validator;

public class ValueElementNode extends ExpressionElementNode {
    public ValueElementNode(Token token)
    {
        super(token);
    }

	@Override
	public String toString() {
		return "Value Element Node: " + this.getToken().tokenType + ", " + this.getToken().tokenValue + "\n";
	}

    @Override
    public void validate(SymbolTableEntry[] entries) throws JPLException {
        if (this.getToken().tokenType == TokenType.Identifier) {
            Validator.validateIdentifierIsDeclared(entries, getToken().tokenValue);
        }
    }

    @Override
    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        return new ArrayList<IntermediateCodeInstruction>();
    }
}
