package com.jpl.fyp.classLibrary.nodes;

public class IfNode extends ContainingNode
{
    public ExpressionNode testExpression;

    public ContainingNode elseNode;

	@Override
	public String toString()
    {
        String allStatementStrings = statements.stream().toString();
		return String.format("If Statement: \n" +
                             "TestExpression: %s\n" +
                             "Statements: %s\n" +
                             "Else: %s",
                             testExpression,
                             allStatementStrings,
                             elseNode);
	}
  
}
