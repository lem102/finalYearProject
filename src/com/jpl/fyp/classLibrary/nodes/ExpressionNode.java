package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class ExpressionNode
{
    public ExpressionElementNode rootExpressionElementNode;

    private TokenType[] legalTypes = {
        TokenType.Add,
        TokenType.Subtract,
        TokenType.Multiply,
        TokenType.Divide,
        TokenType.And,
        TokenType.Or,
        TokenType.Equal,
        TokenType.NotEqual,
        TokenType.GreaterThan,
        TokenType.LessThan,
        TokenType.GreaterThanOrEqualTo,
        TokenType.LessThanOrEqualTo,
        TokenType.Identifier,
        TokenType.Integer,
        TokenType.OpeningParenthesis,
        TokenType.ClosingParenthesis,
        TokenType.Comma, 
    };

    public ExpressionNode(Token[] tokens) throws JPLException {
        this.validateTokens(tokens);
        ExpressionElementNode rootExpressionElementNode = ExpressionParser.parse(tokens);
        this.rootExpressionElementNode = rootExpressionElementNode;
    }

	private void validateTokens(Token[] tokens) throws JPLException {
        for (Token token : tokens) {
            if (this.isTokenIllegal(token)) {
                System.out.println(token);
                throw new JPLException("Expression Node: illegal token");
            }
        }
	}

	private boolean isTokenIllegal(Token token) {
        boolean isLegal = false;
		for (TokenType legalType : legalTypes) {
            if (token.tokenType == legalType) {
                isLegal = true;
            }
        }
        return !isLegal;
	}

	@Override
    public String toString() {
        return rootExpressionElementNode.toString();
    }

    public void validate(SymbolTableEntry[] entries) {
        this.rootExpressionElementNode.validate(entries);
    }
}
