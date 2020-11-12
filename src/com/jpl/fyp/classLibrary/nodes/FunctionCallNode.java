package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallNode extends StatementNode
{
    public String identifier;

    public List<ExpressionNode> arguments;

    public FunctionCallNode()
    {
        arguments = new ArrayList<ExpressionNode>();
    }

    @Override
    public String toString()
    {
        String output = "";
        output += "Function Call:\n";
        output += "Identifier: " + identifier + "\n";

        for (ExpressionNode argument : arguments)
        {
            output += argument + "\n";
        }

        return output;
    }
}
