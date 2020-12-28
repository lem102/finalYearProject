package com.jpl.fyp.classLibrary;

public class SymbolTableEntry {
    private String name;
    private int id;
    private JPLType type;
    
    public SymbolTableEntry(JPLType type, String name) {
        this.type = type;
        this.name = name;
	}

	public void setId(int id) {
        this.id = id;
	}

    @Override
    public String toString()
    {
        return "|" + id + "|" + type + "|" + name + "|\n";
    }
}
