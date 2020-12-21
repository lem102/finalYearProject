package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLException;

public class StatementNode implements Node
{
	@Override
	public int moveIndexToNextStatement(int endOfStatement, int endOfHeader)
    {
		return endOfStatement;
	}

	@Override
	public RootNode addToRootNode(RootNode rootNode) throws JPLException
    {
        if (rootNode.getNestingStatus().isEmpty())
        {
            // System.out.println("ouch");
            something not right, output 275 lines long instead of 205
        }
        else
        {
            rootNode.getNestingStatus().peek().addStatement(this);
        }
		return rootNode;
	}
}
