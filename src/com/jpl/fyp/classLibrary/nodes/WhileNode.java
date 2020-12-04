package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class WhileNode implements ContainingNode
{
    public ExpressionNode testExpression;
	private List<StatementNode> statements;

    public WhileNode(Token[] tokens) throws JPLException
    {
        validateTokens(tokens);
        Token[] expressionTokens = Arrays.copyOfRange(tokens, 2, tokens.length - 1);
        // needs to be changed when expressionNode is refactored :)
        this.testExpression = new ExpressionNode();
        testExpression.expressionTokens = Arrays.asList(expressionTokens);
        this.statements = new ArrayList<StatementNode>();
    }

	private void validateTokens(Token[] tokens) throws JPLException
    {
        if (tokens[0].tokenType != TokenType.While)
        {
            throw new JPLException("While Node : First token is not while keyword.");
        }
        else if (tokens[1].tokenType != TokenType.OpeningParenthesis)
        {
            throw new JPLException("While Node : Second token is not an opening parenthesis.");
        }
        else if (tokens[tokens.length - 2].tokenType != TokenType.ClosingParenthesis)
        {
            throw new JPLException("While Node : Second to last token is not a closing parenthesis.");
        }
        else if (tokens[tokens.length - 1].tokenType != TokenType.OpeningBrace)
        {
            throw new JPLException("While Node : Last token is not an opening brace.");
        }
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

    @Override
	public List<StatementNode> getStatements()
    {
		return this.statements;
	}

	@Override
	public void addStatement(StatementNode statement)
    {
        this.statements.add(statement);
	}
}
