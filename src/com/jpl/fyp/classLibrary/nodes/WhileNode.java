package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;

public class WhileNode extends ContainingNode
{
    public ExpressionNode testExpression;

    public WhileNode()
    {
        statements = new ArrayList<StatementNode>();
    }

    @Override
    public String toString()
    {
        return String.format("While Loop:\n(\nTestExpression: %s)\nLoopStatements:\n%s\n",
                             testExpression,
                             String.join("\n", statements.stream().toString()));
    }
}
