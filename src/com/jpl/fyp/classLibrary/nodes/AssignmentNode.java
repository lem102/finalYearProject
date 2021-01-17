package com.jpl.fyp.classLibrary.nodes;

import java.util.Arrays;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.compilerComponent.Validator;

public class AssignmentNode extends StatementNode
{
    public String assignmentTarget;

    public ExpressionNode expression;

    public AssignmentNode(Token[] tokens) throws JPLException
    {
        this.validateTokens(tokens);
        this.assignmentTarget = tokens[0].tokenValue;
        var expressionTokens = Arrays.copyOfRange(tokens, 2, tokens.length-1);
        this.expression = new ExpressionNode(expressionTokens);
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
    public String toString() {
        return  "Assignment Node:\n"
            + "AssignmentTarget: " + assignmentTarget + "\n"
            + "Expression:\n"
            + "(\n"
            + expression
            + ")\n";
    }

    @Override
    public void validate(SymbolTableEntry[] entries) throws JPLException {
        Validator.validateIdentifierIsDeclared(entries, this.assignmentTarget);
        this.expression.validate(entries);
    }
}
