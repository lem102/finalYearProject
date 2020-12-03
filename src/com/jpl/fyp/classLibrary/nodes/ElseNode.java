package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.List;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class ElseNode implements ContainingNode
{
    private List<StatementNode> statements;

    public ElseNode(Token[] tokens) throws JPLException
    {
        this.validateTokens(tokens);
        
        this.statements = new ArrayList<StatementNode>();
    }

    private void validateTokens(Token[] tokens) throws JPLException
    {
        if (tokens.length != 2)
        {
            throw new JPLException("Else Node : There should only be two tokens.");
        }
        else if (tokens[0].tokenType != TokenType.Else)
        {
            throw new JPLException("Else Node : First token must be an else token.");
        }
        else if (tokens[1].tokenType != TokenType.OpeningBrace)
        {
            throw new JPLException("Else Node : Second token must be an opening brace token.");
        }
	}

	@Override
	public String toString()
    {
        String output = "";
        output += "Else Node:\n";
        output += "Statements:\n";
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
