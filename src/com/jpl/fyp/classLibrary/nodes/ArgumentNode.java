package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLType;

public class ArgumentNode
{
    private JPLType type;
    private String identifier;

    @Override
    public String toString()
    {
        return String.format("Type: %s, Identifier: %s\n", type, identifier);
    }

	public JPLType getType()
    {
		return type;
	}

	public void setType(JPLType type)
    {
		this.type = type;
	}

	public String getIdentifier()
    {
		return identifier;
	}

	public void setIdentifier(String identifier)
    {
		this.identifier = identifier;
	}
}
