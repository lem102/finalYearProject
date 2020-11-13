package com.jpl.fyp.classLibrary.nodes;

public class IfNode extends ContainingNode
{
    public ExpressionNode testExpression;

    public ContainingNode elseNode;

    public IfNode()
    {
        testExpression = new ExpressionNode();
    }

	@Override
	public String toString()
    {
        String output = "";
        output += "If Statement:\n";
        output += "(\n";        
        output += "Expression:\n";
        output += testExpression;
        output += ")\n";

        output += "Statements:\n";
        output += "{\n";
        for (StatementNode statementNode : statements)
        {
            output += statementNode;
        }
        output += "Else:\n";
        output += "{\n";
        output += elseNode + "\n";
        output += "}\n";
        output += "}\n";
        
		return output;
	}
}
