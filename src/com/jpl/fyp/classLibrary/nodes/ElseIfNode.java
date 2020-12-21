package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayDeque;
import java.util.Arrays;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class ElseIfNode extends IfNode
{
	public ElseIfNode(Token[] tokens)
        throws JPLException
    {
        super(prepareTokens(tokens));
	}

	private static Token[] prepareTokens(Token[] tokens) throws JPLException
    {
		if (tokens[0].tokenType != TokenType.Else)
        {
            throw new JPLException("Else If Node : First token must be an else token.");
        }

        Token[] preparedTokens = Arrays.copyOfRange(tokens, 1, tokens.length);

        return preparedTokens;
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
