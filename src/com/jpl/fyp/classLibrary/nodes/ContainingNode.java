package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.List;

public class ContainingNode extends StatementNode
{
    public List<StatementNode> statements;

    public ContainingNode()
    {
        statements = new ArrayList<StatementNode>();
    }

    @Override
	public String toString()
    {
        String output = "";
        output += "Containing Node:\n";
        output += "Statements:\n";
        output += "{\n";
        for (StatementNode statementNode : statements)
        {
            output += statementNode;
        }
        output += "}\n";
        
		return output;
	}
}
