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

	public List<StatementNode> getStatements() {
		return statements;
	}

	public void setStatements(List<StatementNode> statements) {
		this.statements = statements;
	}
}
