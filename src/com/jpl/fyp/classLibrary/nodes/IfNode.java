package com.jpl.fyp.classLibrary.nodes;

import java.util.Arrays;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.compilerComponent.Parser;

public class IfNode extends ContainingNode
{
    public ExpressionNode testExpression;

    public ContainingNode elseNode;

    public IfNode(Token[] tokens, RootNode rootNode)
        throws JPLException
    {
        validateTokens(tokens,rootNode);

        this.testExpression = new ExpressionNode();
        Token[] expressionTokens = Arrays.copyOfRange(tokens, 2, tokens.length - 1);
        this.testExpression.expressionTokens = Arrays.asList(expressionTokens);
    }

	private void validateTokens(Token[] tokens, RootNode rootNode)
        throws JPLException
    {
        if (tokens[0].tokenType != TokenType.If)
        {
            Parser.throwParserException(rootNode, "If Statement : First token must be an if identifier.");
        }
        else if (tokens[1].tokenType != TokenType.OpeningParenthesis)
        {
            Parser.throwParserException(rootNode, "If Statement : Second token must be an opening parenthesis.");
        }
        else if (tokens[tokens.length-1].tokenType != TokenType.ClosingParenthesis)
        {
            Parser.throwParserException(rootNode, "If Statement : Second token must be an opening parenthesis.");
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
}
