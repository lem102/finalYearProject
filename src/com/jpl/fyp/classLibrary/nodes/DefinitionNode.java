package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.List;

public class DefinitionNode implements ContainingNode
{
    public String definitionName;

    public List<ArgumentNode> arguments;

	private List<StatementNode> statements;

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

    @Override
	public List<StatementNode> getStatements()
    {
		return this.statements;
	}

	@Override
	public void addStatement(StatementNode statement)
    {
        this.statements.add(statement);
	}
}
