package com.jpl.fyp.compilerComponent;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.JPLType;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.classLibrary.nodes.ArgumentNode;
import com.jpl.fyp.classLibrary.nodes.AssignmentNode;
import com.jpl.fyp.classLibrary.nodes.ConditionalNode;
import com.jpl.fyp.classLibrary.nodes.ContainingNode;
import com.jpl.fyp.classLibrary.nodes.DeclarationNode;
import com.jpl.fyp.classLibrary.nodes.DefinitionNode;
import com.jpl.fyp.classLibrary.nodes.ElseIfNode;
import com.jpl.fyp.classLibrary.nodes.ElseNode;
import com.jpl.fyp.classLibrary.nodes.FunctionCallNode;
import com.jpl.fyp.classLibrary.nodes.IfNode;
import com.jpl.fyp.classLibrary.nodes.RootNode;
import com.jpl.fyp.classLibrary.nodes.StatementNode;
import com.jpl.fyp.classLibrary.nodes.WhileNode;

public class Parser
{
    public RootNode output;

    public Parser(Token[] tokens) throws JPLException
    {
        this.output = parse(tokens);
    }

	private RootNode parse(Token[] tokens) throws JPLException
    {
        var nestingStatus = new ArrayDeque<ContainingNode>();
        // TODO: in future need to have a symbol table to handle variable and function names.

        // TODO: i should have an arraylist of integers that is changed to
        // reflect the current nesting level of the parse. when a variable
        // is declared, that array is cloned and then stored with the declaration
        // node or in a separate variable. cant decide yet. i think storing
        // within the declaration node is a better idea.
        
        // List< symbolTable = new List<(int, string, object)>();
        var rootNode = new RootNode();

        for (int i = 0; i < tokens.length; i++)
        {
            if (tokens[i].tokenType == TokenType.Define)
            {
                // this function modifies three values, nestingStatus, rootNode AND i.
                // it should only modify 1.
                i = parseDefinition(tokens, nestingStatus, rootNode, i);
            }
            else
            {
                throw new JPLException("all code must be contained within definitions.");
            }
        }

        return rootNode;
	}

	private int parseDefinition(Token[] tokens,
                                ArrayDeque<ContainingNode> nestingStatus,
                                RootNode rootNode,
                                int i)
        throws JPLException
    {
        if (containsDefinitionNode(nestingStatus))
        {
            throw new JPLException("cannot define function inside of function.");
        }
        else
        {
            i = parseDefinitionHeader(tokens, nestingStatus, rootNode, i);
            i = parseStatements(tokens, nestingStatus, i);
        }
		return i;
	}

	private int parseStatements(Token[] tokens,
                                ArrayDeque<ContainingNode> nestingStatus,
                                int i)
        throws JPLException
    {
        while (true)
        {
            if (tokens[i].tokenType == TokenType.ClosingBrace)
            {
                nestingStatus.pop();
                if (nestingStatus.size() <= 0)
                {
                    break;
                }
                i++;
                continue;
            }
            
            int endOfStatement = elementsUntilPastEndOfStatement(tokens, i);
            int endOfHeader = elementsUntilPastEndOfHeader(tokens, i);
            StatementNode node = parseNextStatementOrHeader(tokens, i, endOfStatement, endOfHeader);

            if (node instanceof ElseIfNode
                ||
                node instanceof ElseNode)
            {
                StatementNode previousStatementNode = getLastElement(nestingStatus.peek().getStatements());
		        if (!(previousStatementNode instanceof IfNode))
		        {
                    throw new JPLException("else statement can only occur after an if or else if statement.");
		        }
		        var parentIfNode = (ConditionalNode)previousStatementNode;
		        parentIfNode = getLastOfIfElseChain(parentIfNode);
                parentIfNode.setElseNode((ContainingNode)node);
            }
            else
            {
                nestingStatus.peek().addStatement(node);
            }

            if (node instanceof ContainingNode)
            {
                nestingStatus.push((ContainingNode)node);
                i = endOfHeader;
            }
            else
            {
                i = endOfStatement;
            }
        }
		return i;
	}

	private StatementNode parseNextStatementOrHeader(Token[] tokens,
                                        int i,
                                        int endOfStatement,
                                        int endOfHeader)
        throws JPLException
    {
		switch (tokens[i].tokenType)
		{
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

	private ConditionalNode getLastOfIfElseChain(ConditionalNode parentIfNode)
    {
        while (parentIfNode.getElseNode() != null)
        {
            // TODO: Add a check here to check for rouge else nodes.
            parentIfNode = (IfNode)parentIfNode.getElseNode();
        }
        return parentIfNode;
	}

	private int parseDefinitionHeader(Token[] tokens,
                                      ArrayDeque<ContainingNode> nestingStatus,
                                      RootNode rootNode,
                                      int i)
        throws JPLException
    {
        var definitionNode = new DefinitionNode();
        rootNode.definitions.add(definitionNode);
        nestingStatus.push(definitionNode);
        i++;
        if (tokens[i].tokenType != TokenType.Identifier)
        {
            throw new JPLException("Definition must have a name.");
        }
        definitionNode.definitionName = tokens[i].tokenValue;
        i++;
        if (tokens[i].tokenType != TokenType.OpeningParenthesis)
        {
            throw new JPLException("Definition must have an opening parenthesis after name.");
        }
        i++;
        if (tokens[i].tokenType == TokenType.ClosingParenthesis)
        {
            i++;
        }
        else
        {
            i = parseDefinitionArguments(tokens,
                                         rootNode,
                                         i,
                                         definitionNode);
        }
        if (tokens[i].tokenType != TokenType.OpeningBrace)
        {
            throw new JPLException("Opening brace required after function arguments.");
        }
        i++;
		return i;
	}

	private int parseDefinitionArguments(Token[] tokens,
                                         RootNode rootNode,
                                         int i,
                                         DefinitionNode definitionNode) throws JPLException
    {
        while (true)
        {
            var argumentNode = new ArgumentNode();
            definitionNode.arguments.add(argumentNode);
            
            // this if needs to include all other declaration tokens if/when they are added.
            if (tokens[i].tokenType != TokenType.IntegerDeclaration)
            {
                throw new JPLException("arguments must have a type.");
            }
            argumentNode.type = JPLType.Integer;
            i++;
            if (tokens[i].tokenType != TokenType.Identifier)
            {
                throw new JPLException("arguments must have a identifier after their type.");
            }

            argumentNode.identifier = tokens[i].tokenValue;
            i++;
            if (tokens[i].tokenType == TokenType.ClosingParenthesis)
            {
                i++;
                break;
            }
            else if (tokens[i].tokenType == TokenType.Comma)
            {
                i++;
                continue;
            }
            else
            {
                throw new JPLException("Invalid extra token after argument.");
            }
        }
		return i;
	}

	private boolean containsDefinitionNode(ArrayDeque<ContainingNode> nestingStatus)
    {
        for (ContainingNode containingNode : nestingStatus)
        {
        	if (containingNode instanceof DefinitionNode)
            {
                return true;
            }
        }
		return false;
	}

    private <T> int getLastElementIndex(List<T> arrayList)
    {
        return arrayList.size() - 1;
    }

    private <T> T getLastElement(List<T> arrayList)
    {
        return arrayList.get(getLastElementIndex(arrayList));
    }
}
