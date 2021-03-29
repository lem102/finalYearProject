package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class ExpressionNode
{
    private ExpressionElementNode rootExpressionElementNode;

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
                throw new JPLException("Expression Node: illegal token: " + token);
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

    public void validate(SymbolTableEntry[] entries) throws JPLException {
        this.rootExpressionElementNode.validate(entries);
    }

	public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.addAll(this.rootExpressionElementNode.generateIntermediateCode());
		return instructions;
	}

	public ExpressionElementNode getRootExpressionElementNode() {
		return this.rootExpressionElementNode;
	}
}
