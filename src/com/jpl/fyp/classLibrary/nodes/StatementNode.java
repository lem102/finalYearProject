package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLException;

public class StatementNode implements Node
{
	@Override
	public int moveIndexToNextStatement(int endOfStatement, int endOfHeader)
    {
		return endOfStatement;
	}

	// @Override
	// public RootNode addToRootNode(RootNode rootNode) throws JPLException
    // {
    //     rootNode.getNestingStatus().peek().addStatement(this);
	// 	return rootNode;
	// }
}
