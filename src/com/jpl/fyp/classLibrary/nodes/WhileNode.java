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
        String output = "";
        output += "While Loop:\n";
        output += "(\n";
        output += "Expression:\n";        
        output += testExpression;
        output += ")\n";

        output += "{\n";
        for (StatementNode statementNode : statements)
        {
            output += statementNode;
        }
        output += "}\n";

        return output;
    }
}
