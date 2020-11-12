package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLType;

public class AssignmentNode extends StatementNode
{
    public String assignmentTarget;

    public ExpressionNode expression;

    @Override
    public String toString()
    {
        var output = "Assignment Node:\n";
        output += String.format("AssignmentTarget: %s\n",
                                assignmentTarget);
        output += String.format("Expression: %s\n",
                                expression);
        return output;
    }
}
