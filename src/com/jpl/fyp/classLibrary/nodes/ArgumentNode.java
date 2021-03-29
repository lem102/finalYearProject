package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLType;

public class ArgumentNode
{
    private JPLType type;
    private String name;

    @Override
    public String toString()
    {
        return String.format("Type: %s, Identifier: %s\n", type, name);
    }

	public JPLType getType()
    {
		return type;
	}

	public void setType(JPLType type)
    {
		this.type = type;
	}

	public String getName()
    {
		return name;
	}

	public void getIdentifier(String name)
    {
		this.name = name;
	}
}
