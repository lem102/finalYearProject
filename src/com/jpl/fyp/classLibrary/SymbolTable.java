package com.jpl.fyp.classLibrary;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    private List<SymbolTableEntry> symbols;

    public SymbolTable() {
        symbols = new ArrayList<SymbolTableEntry>();
    }

	public void addSymbol(SymbolTableEntry symbolTableEntry) {
        symbolTableEntry = assignIdToEntry(symbolTableEntry);
        symbols.add(symbolTableEntry);
	}

	private SymbolTableEntry assignIdToEntry(SymbolTableEntry symbolTableEntry) {
        symbolTableEntry.setId(symbols.size());
		return symbolTableEntry;
	}

    @Override
    public String toString() {
        String output = "";
        if (this.symbols.size() > 0) {
            output += "Symbol Table:\n";
            for (SymbolTableEntry symbolTableEntry : symbols) {
                output += symbolTableEntry;
            }
        }
        return output;
    }

	public List<SymbolTableEntry> getSymbols() {
		return symbols;
	}
}
