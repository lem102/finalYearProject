package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class FunctionElementNode extends ExpressionElementNode {

    private FunctionCallNode functionCallNode;

    public FunctionElementNode(Token[] tokens) throws JPLException {
        super(tokens[0]);
        tokens = addSemicolonToEnd(tokens);
        this.functionCallNode = new FunctionCallNode(tokens);
    }

	private Token[] addSemicolonToEnd(Token[] tokens) {
        var tokenList = new ArrayList<Token>();
        Collections.addAll(tokenList, tokens);
        tokenList.add(new Token(TokenType.Semicolon, "Semicolon"));
		return tokenList.toArray(new Token[tokenList.size()]);
	}
}
