package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class StatementNode implements Node
{
    private SymbolTableEntry symbolTableEntry;
    
	@Override
	public int moveIndexToNextStatement(int endOfStatement, int endOfHeader)
    {
		return endOfStatement;
	}

	public SymbolTableEntry getSymbolTableEntry() {
		return symbolTableEntry;
	}

	public void setSymbolTableEntry(SymbolTableEntry symbolTableEntry) {
		this.symbolTableEntry = symbolTableEntry;
	}

	@Override
	public void validate(SymbolTableEntry[] entries) throws JPLException {
        
	}

	@Override
	public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        System.out.println("JACOB WARNING: IR code has not been written for this node.");
        var instructions = new ArrayList<IntermediateCodeInstruction>();
		return instructions;
	}

	public String getExpressionResultVariableName(Token token, ArrayList<IntermediateCodeInstruction> testExpressionInstructions) {
        if (token.tokenType == TokenType.Integer) {
            return token.tokenValue;
        } else {
            return testExpressionInstructions.get(testExpressionInstructions.size() -1).getResult();
        }
	}
}
