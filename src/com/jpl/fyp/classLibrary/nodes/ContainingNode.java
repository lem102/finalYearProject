package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.jpl.fyp.classLibrary.JPLException;

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

    // @Override
    // public ArrayDeque<ContainingNode> updateNestingStatus(ArrayDeque<ContainingNode> nestingStatus)
	// 	throws JPLException
    // {
    //     super.updateNestingStatus(nestingStatus);
    //     nestingStatus.push(this);
    //     return nestingStatus;
    // }

    protected ConditionalNode getLastOfIfElseChain(ConditionalNode parentIfNode)
    {
        while (parentIfNode.getElseNode() != null)
        {
            // TODO: Add a check here to check for rouge else nodes.
            parentIfNode = (IfNode)parentIfNode.getElseNode();
        }
        return parentIfNode;
	}
}
