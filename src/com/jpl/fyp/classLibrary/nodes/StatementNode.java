package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayDeque;

import com.jpl.fyp.classLibrary.JPLException;

public class StatementNode implements Node
{
	@Override
	public int moveIndexToNextStatement(int endOfStatement, int endOfHeader)
    {
		return endOfStatement;
	}

	@Override
	public ArrayDeque<ContainingNode> updateNestingStatus(ArrayDeque<ContainingNode> nestingStatus) throws JPLException
    {
        nestingStatus.peek().addStatement(this);
		return nestingStatus;
	}
}
