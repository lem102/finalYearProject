package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.compilerComponent.Parser;

public class FunctionCallNode extends StatementNode
{
    public String identifier;

    public ExpressionNode[] arguments;

    public FunctionCallNode(Token[] tokens, RootNode rootNode)
        throws JPLException
    {
        this.validateTokens(tokens, rootNode);
        this.identifier = tokens[0].tokenValue;
        Token[] argumentTokens = Arrays.copyOfRange(tokens, 2, tokens.length - 1);
        this.arguments = parseArgumentTokens(argumentTokens);
    }

    private ExpressionNode[] parseArgumentTokens(Token[] tokens)
    {
        var output = new ArrayList<ExpressionNode>();
        List<Token[]> splitArgumentTokens = splitTokensByArgument(tokens);

        for (Token[] tokenArray : splitArgumentTokens)
        {
            // this will need to be refactored when expressionNode is refactored.
            var node = new ExpressionNode();
            output.add(node);
            node.expressionTokens = Arrays.asList(tokenArray);
        }

        return output.toArray(new ExpressionNode[output.size()]);
	}

	private List<Token[]> splitTokensByArgument(Token[] tokens)
    {
        int argumentStartIndex = 0;
        List<Token[]> output = new ArrayList<Token[]>();

        for (int tokenIndex = 0; tokenIndex < tokens.length; tokenIndex++)
        {
            if (tokens[tokenIndex].tokenType == TokenType.Comma)
            {
                output.add(Arrays.copyOfRange(tokens, argumentStartIndex, tokenIndex));
                argumentStartIndex = tokenIndex + 1;
            }
        }

        output.add(Arrays.copyOfRange(tokens, argumentStartIndex, tokens.length));
        
        return output;
	}

	private void validateTokens(Token[] tokens, RootNode rootNode) throws JPLException
    {
        if (tokens[0].tokenType != TokenType.Identifier)
        {
            Parser.throwParserException(rootNode, "Function Call Node : first token must be an identifier.");
        }
        else if (tokens[1].tokenType != TokenType.OpeningParenthesis)
        {
            Parser.throwParserException(rootNode, "Function Call Node : second token must be a opening parenthesis.");
        }
        else if (tokens[tokens.length - 1].tokenType != TokenType.ClosingParenthesis)
        {
            Parser.throwParserException(rootNode, "Function Call Node : last token must be a closing parenthesis.");
        }
	}

	@Override
    public String toString()
    {
        String output = "";
        output += "Function Call:\n";
        output += "Identifier: " + identifier + "\n";

        for (ExpressionNode argument : arguments)
        {
            output += argument + "\n";
        }

        return output;
    }
}
