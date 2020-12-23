package com.jpl.fyp.classLibrary.nodes;

public class StatementNode implements Node
{
	@Override
	public int moveIndexToNextStatement(int endOfStatement, int endOfHeader)
    {
		return endOfStatement;
	}
}
