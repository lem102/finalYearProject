package com.jpl.fyp.classLibrary.nodes;

public class BinaryComparisonNode extends ExpressionElementNode
{
    private JPLBinaryComparison comparison;

	public BinaryComparisonNode(JPLBinaryComparison comparison)
    {
        this.comparison = comparison;
	}

	public JPLBinaryComparison getComparison()
    {
		return comparison;
	}
}
