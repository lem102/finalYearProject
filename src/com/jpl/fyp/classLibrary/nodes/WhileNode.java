package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.compilerComponent.Parser;

public class WhileNode extends ContainingNode
{
    public ExpressionNode testExpression;

    public WhileNode(Token[] tokens, RootNode rootNode) throws JPLException
    {
        validateTokens(tokens, rootNode);
        Token[] expressionTokens = Arrays.copyOfRange(tokens, 2, tokens.length - 1);
        // needs to be changed when expressionNode is refactored :)
        this.testExpression = new ExpressionNode();
        testExpression.expressionTokens = Arrays.asList(expressionTokens);
        this.statements = new ArrayList<StatementNode>();
    }

	private void validateTokens(Token[] tokens,
                                RootNode rootNode) throws JPLException
    {
        if (tokens[0].tokenType != TokenType.While)
        {
            Parser.throwParserException(rootNode,
                                        "While Node : first token is not while keyword.");
        }
        else if (tokens[1].tokenType != TokenType.OpeningParenthesis)
        {
            Parser.throwParserException(rootNode,
                                        "While Node : second token is not an opening parenthesis.");
        }
        else if (tokens[tokens.length - 1].tokenType != TokenType.ClosingParenthesis)
        {
            Parser.throwParserException(rootNode,
                                        "While Node : second to last token is not a closing parenthesis.");
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
}
