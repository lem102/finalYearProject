package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.List;

import com.jpl.fyp.classLibrary.Token;

public class ExpressionNode
{
    public List<Token> expressionTokens;

    public ExpressionNode()
    {
        expressionTokens = new ArrayList<Token>();
    }

    @Override
    public String toString()
    {
        String output = "";

        for (Token token : expressionTokens)
        {
            output += token + "\n";
        }
        
        return output;
    }
}
