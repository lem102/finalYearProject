package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.List;

public class DefinitionNode extends ContainingNode
{
    private String definitionName;

    private List<ArgumentNode> arguments;

    public DefinitionNode() 
    {
        arguments = new ArrayList<ArgumentNode>();
        statements = new ArrayList<StatementNode>();
    }

    public String ToString()
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
    
	public String getDefinitionName()
    {
		return definitionName;
	}
    
	public void setDefinitionName(String definitionName)
    {
		this.definitionName = definitionName;
	}
    
	public List<ArgumentNode> getArguments()
    {
		return arguments;
	}
    
	public void setArguments(List<ArgumentNode> arguments)
    {
		this.arguments = arguments;
	}
}
