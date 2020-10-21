package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLType;

public class ArgumentNode
{
    public JPLType type;
        
    public String identifier;

    public String ToString()
    {
        return String.format("Type: %s, Identifier: %s\n", type, identifier);
    }
}
