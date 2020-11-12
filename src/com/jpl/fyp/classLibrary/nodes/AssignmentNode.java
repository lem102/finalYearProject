package com.jpl.fyp.classLibrary.nodes;

public class AssignmentNode extends StatementNode
{
    public String assignmentTarget;

    public ExpressionNode expression;

    @Override
    public String toString()
    {
        var output = "Assignment Node:\n";
        output += "AssignmentTarget: " + assignmentTarget + "\n";
        output += "Expression:\n";
        output += "(\n";
        output += expression;
        output += ")\n";
        return output;
    }
}
