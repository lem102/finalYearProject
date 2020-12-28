package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class FunctionCallNode extends StatementNode {
    public String identifier;

    public ExpressionNode[] arguments;

    public FunctionCallNode(Token[] tokens) throws JPLException {
        this.validateTokens(tokens);
        this.identifier = tokens[0].tokenValue;
        Token[] argumentTokens = Arrays.copyOfRange(tokens, 2, tokens.length - 2);
        this.arguments = parseArgumentTokens(argumentTokens);
    }

    private ExpressionNode[] parseArgumentTokens(Token[] tokens) throws JPLException {
        var output = new ArrayList<ExpressionNode>();

        for (Token[] tokenArray : splitTokensByArgument(tokens))
        {
            output.add(new ExpressionNode(tokenArray));
        }

        System.out.println("parse arg tokens");
        for (Token token : tokens) {
            System.out.println(token);
        }


        return output.toArray(new ExpressionNode[output.size()]);
	}

	private Token[][] splitTokensByArgument(Token[] tokens) {
        int argumentStartIndex = 0;
        List<Token[]> output = new ArrayList<Token[]>();

        for (int tokenIndex = 0; tokenIndex < tokens.length; tokenIndex++) {
            if (tokens[tokenIndex].tokenType == TokenType.Comma) {
                output.add(Arrays.copyOfRange(tokens, argumentStartIndex, tokenIndex));
                argumentStartIndex = tokenIndex + 1;
            }
        }

        output.add(Arrays.copyOfRange(tokens, argumentStartIndex, tokens.length));
        
        return output.toArray(new Token[output.size()][]);
	}

	private void validateTokens(Token[] tokens) throws JPLException
    {
        System.out.println("test");
        for (Token temp : tokens) {
            System.out.println(temp);
        }


        
        if (tokens[0].tokenType != TokenType.Identifier) {
            throw new JPLException("Function Call Node : First token must be an identifier.");
        }
        else if (tokens[1].tokenType != TokenType.OpeningParenthesis) {
            throw new JPLException("Function Call Node : Second token must be a opening parenthesis.");
        }
        else if (tokens[tokens.length - 2].tokenType != TokenType.ClosingParenthesis) {
            throw new JPLException("Function Call Node : Second to last token must be a closing parenthesis.");
        }
        else if (tokens[tokens.length - 1].tokenType != TokenType.Semicolon) {
            throw new JPLException("Function Call Node : Last token must be a semicolon.");
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
