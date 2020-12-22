package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.List;

public class ContainingNode extends StatementNode
{
    private List<StatementNode> statements;

    public ContainingNode()
    {
        this.statements = new ArrayList<StatementNode>();
    }

	public List<StatementNode> getStatements()
    {
        return this.statements;
    };

	public void addStatement(StatementNode statement)
    {
        this.statements.add(statement);
	}

	@Override
	public int moveIndexToNextStatement(int endOfStatement, int endOfHeader)
    {
		return endOfHeader;
	}
}
