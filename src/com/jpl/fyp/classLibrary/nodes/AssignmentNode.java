package com.jpl.fyp.classLibrary.nodes;

import java.util.Arrays;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.compilerComponent.Parser;

public class AssignmentNode extends StatementNode
{
    public String assignmentTarget;

    public ExpressionNode expression;

    public AssignmentNode(Token[] tokens,
                          RootNode rootNode) throws JPLException
    {
        validateTokens(tokens, rootNode);
        this.assignmentTarget = tokens[0].tokenValue;
        // this will need changing when expression node is refactored.
        this.expression = new ExpressionNode();
        this.expression.expressionTokens = Arrays.asList(Arrays.copyOfRange(tokens,
                                                                            2,
                                                                            tokens.length));
    }

    private void validateTokens(Token[] tokens,
                                RootNode rootNode) throws JPLException
    {
        if (tokens[0].tokenType != TokenType.Identifier)
        {
            Parser.throwParserException(rootNode,
                                        "Assignment Node : first token is not an identifier token.");
        }
        else if (tokens[1].tokenType != TokenType.Assignment)
        {
            Parser.throwParserException(rootNode,
                                        "Assignment Node : second token is not an assignment token.");
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
