package com.jpl.fyp.compilerComponent;

import java.util.Arrays;

import com.jpl.fyp.classLibrary.*;
import com.jpl.fyp.classLibrary.nodes.*;

public class Parser
{
    public static RootNode parse(Token[] tokens) throws JPLException {
        var rootNode = new RootNode();
        int tokenIndex = 0;
        
        while(tokenIndex <= tokens.length - 1) {
            if (tokens[tokenIndex].tokenType == TokenType.ClosingBrace) {
                rootNode.getNestingStatus().pop();
                tokenIndex++;
                continue;
            }
            
            StatementNode node = parseNextStatementOrHeader(tokens, tokenIndex);
            rootNode.addNode(node);

            int endOfStatement = elementsUntilPastEndOfStatement(tokens, tokenIndex);
            int endOfHeader = elementsUntilPastEndOfHeader(tokens, tokenIndex);
            tokenIndex = node.moveIndexToNextStatement(endOfStatement, endOfHeader);
        }
        return rootNode;
	}

	private static StatementNode parseNextStatementOrHeader(Token[] tokens, int tokenIndex) throws JPLException {
        int endOfStatement = elementsUntilPastEndOfStatement(tokens, tokenIndex);
        int endOfHeader = elementsUntilPastEndOfHeader(tokens, tokenIndex);
        
		switch (tokens[tokenIndex].tokenType)
		{
            case Define:
            {
                Token[] relevantTokens = Arrays.copyOfRange(tokens, tokenIndex, endOfHeader);
                return new DefinitionNode(relevantTokens);
            } 
		    case IntegerDeclaration:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, tokenIndex, endOfStatement);
		        return new DeclarationNode(relevantTokens);
		    }
		    case Identifier:
		    {
		        return parseStatementBeginningWithIdentifier(tokens, tokenIndex, endOfStatement);
		    }
		    case While:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, tokenIndex, endOfHeader);
		        return new WhileNode(relevantTokens);
		    }
		    case If:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, tokenIndex, endOfHeader);
		        return new IfNode(relevantTokens);
		    }
		    case Else:
		    {
		        return parseStatementBeginningWithElse(tokens, tokenIndex, endOfHeader);
		    }
		    default:
		    {
		        throw new JPLException("unhandled token");
		    }
		}
	}

	private static StatementNode parseStatementBeginningWithElse(Token[] tokens,
                                                          int i,
                                                          int endOfHeader)
        throws JPLException
    {
        switch (tokens[i+1].tokenType)
        {
            case If:
            {
                Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfHeader);
                return new ElseIfNode(relevantTokens);
            }
            case OpeningBrace:
            {
                Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfHeader);
                return new ElseNode(relevantTokens);
            }
            default:
            {
                throw new JPLException("else token must be followed by if " +
                                       "token incase of else if statement or opening parenthesis " +
                                       "in the case of a straight else statement.");
            }
        }
	}

	private static StatementNode parseStatementBeginningWithIdentifier(Token[] tokens,
                                                                int i,
                                                                int endOfStatement)
        throws JPLException
    {
		switch (tokens[i+1].tokenType)
		{
		    case Assignment:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfStatement);
		        return new AssignmentNode(relevantTokens);
		    }
		    case OpeningParenthesis:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfStatement);
		        return new FunctionCallNode(relevantTokens);
		    }
		    default:
		    {
		        throw new JPLException("Invalid token, was expecting either an opening brace token " +
		                               "in case of a standalone method call or an assignment " +
		                               "token in case of variable assignment.");
		    }
		}
	}

    private static int elementsUntilPastEndOfStatement(Token[] tokens,
                                                int startIndex)
    {
        return elementsUntilPastNextOccuranceOfToken(tokens,
                                                     startIndex,
                                                     TokenType.Semicolon);
    }

    private static int elementsUntilPastEndOfHeader(Token[] tokens,
                                             int startIndex)
    {
		return elementsUntilPastNextOccuranceOfToken(tokens,
                                                     startIndex,
                                                     TokenType.OpeningBrace);
    }

    private static int elementsUntilPastNextOccuranceOfToken(Token[] tokens,
                                                      int startIndex,
                                                      TokenType tokenType)
    {
        try
        {
            while (tokens[startIndex].tokenType != tokenType)
            {
                startIndex++;
                // you are thinking about refactoring this method.
            }
            startIndex++;
            return startIndex;
        }
        catch (Throwable e)
        {
            return 0;
        }
    }
}
