package com.jpl.fyp.classLibrary.nodes;

public class BinaryOperatorNode extends ExpressionElementNode
{
    private JPLBinaryOperator operator;

    public BinaryOperatorNode(JPLBinaryOperator operator)
    {
        this.operator = operator;
    }

	public JPLBinaryOperator getOperator()
    {
		return operator;
	}
}
