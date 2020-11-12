package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLType;

public class DeclarationNode extends StatementNode
{
    public JPLType type;

    public String identifier;

    public ExpressionNode expression;

    @Override
    public String toString()
    {
        String output = "";
        output += "Declaration:\n";
        output += "Type: " + type + "\n";
        output += "Identifier: " + identifier + "\n";
        output += "Expression:\n";
        output += expression + "\n";
        
        return output;
    }
}
