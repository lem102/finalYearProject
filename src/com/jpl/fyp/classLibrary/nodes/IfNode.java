package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.compilerComponent.Parser;

public class IfNode implements ConditionalNode
{
    public ExpressionNode testExpression;

    private ContainingNode elseNode;

	private List<StatementNode> statements;

    public IfNode(Token[] tokens)
        throws JPLException
    {
        validateTokens(tokens);
        this.statements = new ArrayList<StatementNode>();

        this.testExpression = new ExpressionNode();
        Token[] expressionTokens = Arrays.copyOfRange(tokens, 2, tokens.length - 2);
        this.testExpression.expressionTokens = Arrays.asList(expressionTokens);
    }

	private void validateTokens(Token[] tokens)
        throws JPLException
    {
        if (tokens[0].tokenType != TokenType.If)
        {
            throw new JPLException("If Statement : First token must be an if identifier.");
        }
        else if (tokens[1].tokenType != TokenType.OpeningParenthesis)
        {
            throw new JPLException("If Statement : Second token must be an opening parenthesis.");
        }
        else if (tokens[tokens.length-2].tokenType != TokenType.ClosingParenthesis)
        {
            throw new JPLException("If Statement : Second to last token must be a closing parenthesis.");
        }
        else if (tokens[tokens.length-1].tokenType != TokenType.OpeningBrace)
        {
            throw new JPLException("If Statement : Last token must be an opening brace.");
        }
	}

	@Override
	public String toString()
    {
        String output = "";
        output += "If Statement:\n";
        output += "(\n";        
        output += "Expression:\n";
        output += testExpression;
        output += ")\n";

        output += "Statements:\n";
        output += "{\n";
        for (StatementNode statementNode : statements)
        {
            output += statementNode;
        }
        output += "Else:\n";
        output += "{\n";
        output += elseNode + "\n";
        output += "}\n";
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

	@Override
	public ContainingNode getElseNode()
    {
		return this.elseNode;
	}

	@Override
	public void setElseNode(ContainingNode elseNode)
    {
		this.elseNode = elseNode;
	}
}
