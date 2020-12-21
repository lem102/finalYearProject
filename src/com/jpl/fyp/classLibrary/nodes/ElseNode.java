package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayDeque;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class ElseNode extends ContainingNode
{
    public ElseNode(Token[] tokens) throws JPLException
    {
        super();
        this.validateTokens(tokens);
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
        for (StatementNode statementNode : super.getStatements())
        {
            output += statementNode;
        }
        output += "}\n";
        
		return output;
	}

    @Override
    public RootNode addToRootNode(RootNode rootNode) throws JPLException
    {
        StatementNode previousStatementNode = rootNode.getNestingStatus().peek().getStatements().get(rootNode.getNestingStatus().peek().getStatements().size() - 1);
        if (!(previousStatementNode instanceof IfNode))
        {
            throw new JPLException("else statement can only occur after an if or else if statement.");
        }
        var parentIfNode = (ConditionalNode)previousStatementNode;
        parentIfNode = getLastOfIfElseChain(parentIfNode);
        parentIfNode.setElseNode(this);
        rootNode = super.addToRootNode(rootNode);
        return rootNode;
    }
}
