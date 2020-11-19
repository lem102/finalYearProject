package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.List;

public class DefinitionNode extends ContainingNode
{
    public String definitionName;

    public List<ArgumentNode> arguments;

    public DefinitionNode() 
    {
        arguments = new ArrayList<ArgumentNode>();
        statements = new ArrayList<StatementNode>();
    }

    @Override
    public String toString()
    {
        String output = String.format("function: %s\n(\n", definitionName);

        for (ArgumentNode argument : arguments)
        {
            output = output + argument;
        }

        output = output + ")\n{\n";

        for (StatementNode statement : statements)
        {
            output = output + statement;
        }

        output = output + "}\n";
        return output;
    }
}
