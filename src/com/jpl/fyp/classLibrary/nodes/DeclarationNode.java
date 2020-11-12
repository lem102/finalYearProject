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
        return String.format("Declaration:\nType: %s\nIdentifier: %s\nExpression: %s",
                             type,
                             identifier,
                             expression);
    }
}
