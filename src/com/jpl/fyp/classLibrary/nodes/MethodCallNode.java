package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.List;

public class MethodCallNode extends StatementNode
{
    public String identifier;

    public List<ExpressionNode> arguments;

    public MethodCallNode()
    {
        arguments = new ArrayList<ExpressionNode>();
    }
}
