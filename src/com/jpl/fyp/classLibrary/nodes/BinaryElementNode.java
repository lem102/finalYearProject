package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class BinaryElementNode extends ExpressionElementNode
{
    private ExpressionElementNode leftSide;

    private ExpressionElementNode rightSide;

    public BinaryElementNode(TokenType tokenType, Token[] rightSide, Token[] leftSide) throws JPLException
    {
        super(tokenType);
        this.leftSide = ExpressionNode.parse(leftSide);
        this.rightSide = ExpressionNode.parse(rightSide);
    }

    // private ExpressionElementNode parse(Token[] tokens) throws JPLException
    // {
        
        
    //     throw new JPLException("not implemented lol");

    //     // return null;
    // }
}
