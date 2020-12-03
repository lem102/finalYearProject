package com.jpl.fyp.classLibrary.nodes;

import java.util.Arrays;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.JPLType;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.compilerComponent.Parser;

public class DeclarationNode implements StatementNode
{
    public JPLType type;

    public String identifier;

    public ExpressionNode expression;

    public DeclarationNode(Token[] tokens, RootNode rootNode)
        throws JPLException
    {
        this.validateTokens(tokens, rootNode);
        this.type = this.tokenToType(tokens[0]);
        this.identifier = tokens[1].tokenValue;

        // TODO: needs to be changed when expression node is refactored
        if (tokens.length > 2)
        {
            this.expression = new ExpressionNode();
            Token[] expressionTokens = Arrays.copyOfRange(tokens, 3, tokens.length);
            expression.expressionTokens = Arrays.asList(expressionTokens);
        }
	}

	private JPLType tokenToType(Token token) throws JPLException
    {
        if (token.tokenType == TokenType.IntegerDeclaration)
        {
            return JPLType.Integer;
        }
        else
        {
            throw new JPLException("Declaration Node : Invalid type.");
        }
	}

	private void validateTokens(Token[] tokens, RootNode rootNode)
        throws JPLException
    {
        if (tokens[0].tokenType != TokenType.IntegerDeclaration)
        {
            Parser.throwParserException(rootNode,
                                        "Declaration Node : First token must be a declaration token. Currently there are only integer declarations available.");
        }
        else if (tokens[1].tokenType != TokenType.Identifier)
        {
            Parser.throwParserException(rootNode,
                                        "Declaration Node : Second token must be an identifier.");
        }
        else if (tokens[2].tokenType != TokenType.Assignment && tokens[2].tokenType != TokenType.Semicolon)
        {
            Parser.throwParserException(rootNode,
                                        "Declaration Node : Third token must be either an assignment symbol or a semicolon.");
        }
        else if (tokens[tokens.length - 1].tokenType != TokenType.Semicolon)
        {
            Parser.throwParserException(rootNode,
                                        "Declaration Node : Last token must be a semicolon.");
        }
	}

	@Override
    public String toString()
    {
        String output = "";
        output += "Declaration:\n";
        output += "Type: " + type + "\n";
        output += "Identifier: " + identifier + "\n";
        output += "Expression:\n";
        output += expression + "\n";
        
        return output;
    }
}
