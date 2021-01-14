package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;

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
}
