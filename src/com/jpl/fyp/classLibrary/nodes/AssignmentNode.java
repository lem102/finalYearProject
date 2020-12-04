package com.jpl.fyp.classLibrary.nodes;

import java.util.Arrays;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.compilerComponent.Parser;

public class AssignmentNode implements StatementNode
{
    public String assignmentTarget;

    public ExpressionNode expression;

    public AssignmentNode(Token[] tokens) throws JPLException
    {
        this.validateTokens(tokens);
        this.assignmentTarget = tokens[0].tokenValue;
        // this will need changing when expression node is refactored.
        this.expression = new ExpressionNode();
        this.expression.expressionTokens = Arrays.asList(Arrays.copyOfRange(tokens,
                                                                            2,
                                                                            tokens.length));
    }

    private void validateTokens(Token[] tokens) throws JPLException
    {
        if (tokens[0].tokenType != TokenType.Identifier)
        {
            throw new JPLException("Assignment Node : First token is not an identifier token.");
        }
        else if (tokens[1].tokenType != TokenType.Assignment)
        {
            throw new JPLException("Assignment Node : Second token is not an assignment token.");
        }
        else if (tokens[tokens.length - 1].tokenType != TokenType.Semicolon)
        {
            throw new JPLException("Assignment Node : Last token should be a semicolon.");
        }
	}

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
