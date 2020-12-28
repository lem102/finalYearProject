package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;

public class BinaryElementNode extends ExpressionElementNode
{
    private ExpressionElementNode leftSide;

    private ExpressionElementNode rightSide;

    public BinaryElementNode(Token token, Token[] rightSide, Token[] leftSide) throws JPLException
    {
        super(token);

        this.leftSide = ExpressionParser.parse(leftSide);
        this.rightSide = ExpressionParser.parse(rightSide);
    }

	public ExpressionElementNode getLeftSide() {
		return leftSide;
	}

	public ExpressionElementNode getRightSide() {
		return rightSide;
	}

    @Override
    public String toString() {
        return "Binary Element Node:\n" +
            "Type : " + getToken().tokenType + "\n" +
            "{\n" +
            "Left Side:\n" +
            "{\n" +
            leftSide.toString() + "\n" +
            "}\n" +
            "Right Side:\n" +
            "{\n" +
            rightSide.toString() + "\n" +
            "}\n" +
            "}\n";
    }
}
