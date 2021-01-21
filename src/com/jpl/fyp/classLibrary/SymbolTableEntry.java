package com.jpl.fyp.classLibrary;

import com.jpl.fyp.classLibrary.nodes.ArgumentNode;

public class SymbolTableEntry {
    private String name;
    private int id;
    private ArgumentNode[] arguments;
    private JPLType type;
    
    public SymbolTableEntry(JPLType type, String name, ArgumentNode[] arguments) {
        this(type, name);
        this.arguments = arguments;
	}

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

    public String getName() {
        return this.name;
    }

    public ArgumentNode[] getArguments() {
        return this.arguments;
    }
}
