package com.jpl.fyp.compilerComponent;

import java.util.Arrays;

import com.jpl.fyp.classLibrary.*;
import com.jpl.fyp.classLibrary.nodes.*;

public class Parser
{
    public RootNode output;

    public Parser(Token[] tokens) throws JPLException
    {
        this.output = parse(tokens);
    }

	private RootNode parse(Token[] tokens) throws JPLException
    {
        // TODO: in future need to have a symbol table to handle variable and function names.
        // List< symbolTable = new List<(int, string, object)>();
        var rootNode = new RootNode();

        int tokenIndex = 0;
        
        while(tokenIndex <= tokens.length - 1)
        {
            if (tokens[tokenIndex].tokenType == TokenType.ClosingBrace)
            {
                rootNode.getNestingStatus().pop();
                tokenIndex++;
                continue;
            }
            
            int endOfStatement = elementsUntilPastEndOfStatement(tokens, tokenIndex);
            int endOfHeader = elementsUntilPastEndOfHeader(tokens, tokenIndex);
            StatementNode node = parseNextStatementOrHeader(tokens, tokenIndex, endOfStatement, endOfHeader);

            rootNode.addNode(node);

            tokenIndex = node.moveIndexToNextStatement(endOfStatement, endOfHeader);
        }
        return rootNode;
	}

	private StatementNode parseNextStatementOrHeader(Token[] tokens,
                                                     int i,
                                                     int endOfStatement,
                                                     int endOfHeader)
        throws JPLException
    {
		switch (tokens[i].tokenType)
		{
            case Define:
            {
                Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfHeader);
                return new DefinitionNode(relevantTokens);
            } 
		    case IntegerDeclaration:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfStatement);
		        return new DeclarationNode(relevantTokens);
		    }
		    case Identifier:
		    {
		        return parseStatementBeginningWithIdentifier(tokens, i, endOfStatement);
		    }
		    case While:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfHeader);
		        return new WhileNode(relevantTokens);
		    }
		    case If:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfHeader);
		        return new IfNode(relevantTokens);
		    }
		    case Else:
		    {
		        return parseStatementBeginningWithElse(tokens, i, endOfHeader);
		    }
		    default:
		    {
		        throw new JPLException("unhandled token");
		    }
		}
	}

	private StatementNode parseStatementBeginningWithElse(Token[] tokens,
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

	private StatementNode parseStatementBeginningWithIdentifier(Token[] tokens,
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

    private int elementsUntilPastEndOfStatement(Token[] tokens,
                                                int startIndex)
    {
        return elementsUntilPastNextOccuranceOfToken(tokens,
                                                     startIndex,
                                                     TokenType.Semicolon);
    }

    private int elementsUntilPastEndOfHeader(Token[] tokens,
                                             int startIndex)
    {
		return elementsUntilPastNextOccuranceOfToken(tokens,
                                                     startIndex,
                                                     TokenType.OpeningBrace);
    }

    private int elementsUntilPastNextOccuranceOfToken(Token[] tokens,
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
