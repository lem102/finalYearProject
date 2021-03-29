package com.jpl.fyp.classLibrary.nodes;

import java.util.Arrays;

import com.jpl.fyp.classLibrary.*;

public class ExpressionParser {
    public static ExpressionElementNode parse(Token[] tokens) throws JPLException {
        return createElement(tokens, findRootElementIndex(tokens));
	}

    private static ExpressionElementNode createElement(Token[] tokens, int tokenIndex) throws JPLException {
        Token[] leftSide = Arrays.copyOfRange(tokens, 0, tokenIndex);
        Token[] rightSide = Arrays.copyOfRange(tokens, tokenIndex+1, tokens.length);
        Token token = tokens[tokenIndex];

        switch (token.tokenType) {
            case Add:
            case Subtract:
            case Multiply:
            case Divide:
            case Equal:
            case NotEqual:
            case GreaterThan:
            case LessThan:
            case GreaterThanOrEqualTo:
            case LessThanOrEqualTo:
            case And:
            case Or: {
                return new BinaryElementNode(token, rightSide, leftSide);
            }
		    case Integer:
            case Identifier:
            case ClosingParenthesis: {
                if (tokens.length == 1) {
                    return new ValueElementNode(token);
                } else {
                    return new FunctionElementNode(tokens);
                }
            }
            // case OpeningParenthesis:   { return createExpressionNode(elements, tokenIndex); }
		    default: {
                throw new JPLException("Expression Node : Invalid token for expression. ");
            }
		}
	}

	private static int findRootElementIndex(Token[] tokens) throws JPLException {
        // Order is important.
        TokenType[] types = {
            TokenType.And,
            TokenType.Or,
            TokenType.Equal,
            TokenType.NotEqual,
            TokenType.GreaterThan,
            TokenType.LessThan,
            TokenType.GreaterThanOrEqualTo,
            TokenType.LessThanOrEqualTo,
            TokenType.Add,
            TokenType.Subtract,
            TokenType.Multiply,
            TokenType.Divide,
        };

        for (TokenType type : types) {
            if (tokensContainType(tokens, type)) {
                return findFirstOccuranceOfTypeInTokens(tokens, type);
            }
        }
        return 0;
	}

	private static boolean tokensContainType(Token[] tokens, TokenType type) {
		return findFirstOccuranceOfTypeInTokens(tokens, type) != -1;
	}

    private static int findFirstOccuranceOfTypeInTokens(Token[] tokens, TokenType type) {
        int bracketNestingDepth = 0;
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].tokenType == TokenType.OpeningParenthesis) {
                bracketNestingDepth++;
            } else if (tokens[i].tokenType == TokenType.ClosingParenthesis) {
                bracketNestingDepth--;
            }
            
            if (tokens[i].tokenType == type && bracketNestingDepth == 0) {
                return i;
            }
        }
        return -1;
	}
}
