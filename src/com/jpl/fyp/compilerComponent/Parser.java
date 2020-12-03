package com.jpl.fyp.compilerComponent;

import java.util.ArrayDeque;
import java.util.ArrayList;
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
import com.jpl.fyp.classLibrary.nodes.ExpressionNode;
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
                throwParserException(rootNode,
                                     "all code must be contained within definitions.");
            }
        }

        return rootNode;
	}

	public static void throwParserException(RootNode rootNode,
                                            String message) throws JPLException
    {
        System.out.println(rootNode);
        throw new JPLException(message);
	}

	private int parseDefinition(Token[] tokens,
                                ArrayDeque<ContainingNode> nestingStatus,
                                RootNode rootNode,
                                int i) throws JPLException
    {
        if (containsDefinitionNode(nestingStatus))
        {
            throwParserException(rootNode,
                                 "cannot define function inside of function.");
        }
        else
        {
            i = parseDefinitionDeclaration(tokens,
                                           nestingStatus,
                                           rootNode,
                                           i);

            i = parseStatements(tokens,
                                nestingStatus,
                                rootNode,
                                i);
        }
		return i;
	}

	private int parseStatements(Token[] tokens,
                                ArrayDeque<ContainingNode> nestingStatus,
                                RootNode rootNode,
                                int i) throws JPLException
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
            
            int endOfStatement = findEndOfStatement(tokens, i);
            int endOfHeader = findEndOfHeader(tokens, i);
            StatementNode node = parseNextNode(tokens, rootNode, i, endOfStatement, endOfHeader);

            if (node instanceof ElseIfNode
                ||
                node instanceof ElseNode)
            {
                StatementNode previousStatementNode = getLastElement(nestingStatus.peek().getStatements());
		        if (!(previousStatementNode instanceof IfNode))
		        {
                    throwParserException(rootNode,
                                         "else statement can only occur after an if or else if statement.");
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

	private StatementNode parseNextNode(Token[] tokens,
                                        RootNode rootNode,
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
		        return new DeclarationNode(relevantTokens, rootNode);
		    }
		    case Identifier:
		    {
		        return parseNodeBeginningWithIdentifier(tokens, rootNode, i, endOfStatement);
		    }
		    case While:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfHeader);
		        return new WhileNode(relevantTokens, rootNode);
		    }
		    case If:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfHeader);
		        return new IfNode(relevantTokens, rootNode);
		    }
		    case Else:
		    {
		        return parseNodeBeginningWithElse(tokens, rootNode, i, endOfHeader);
		    }
		    default:
		    {
		        throw new JPLException("unhandled token" + "\n" + rootNode);
		    }
		}
	}

	private StatementNode parseNodeBeginningWithElse(Token[] tokens,
                                                     RootNode rootNode,
                                                     int i,
                                                     int endOfHeader)
        throws JPLException
    {
        switch (tokens[i+1].tokenType)
        {
            case If:
            {
                Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfHeader);
                return new ElseIfNode(relevantTokens, rootNode);
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
                                       "in the case of a straight else statement." +
                                       "\n" + rootNode);
            }
        }
	}

	private StatementNode parseNodeBeginningWithIdentifier(Token[] tokens,
                                                           RootNode rootNode,
                                                           int i,
                                                           int endOfStatement)
        throws JPLException
    {
		switch (tokens[i+1].tokenType)
		{
		    case Assignment:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfStatement);
		        return new AssignmentNode(relevantTokens, rootNode);
		    }
		    case OpeningParenthesis:
		    {
		        Token[] relevantTokens = Arrays.copyOfRange(tokens, i, endOfStatement);
		        return new FunctionCallNode(relevantTokens, rootNode);
		    }
		    default:
		    {
		        throw new JPLException("Invalid token, was expecting either an opening brace token " +
		                               "in case of a standalone method call or an assignment " +
		                               "token in case of variable assignment." +
		                               "\n" + rootNode);
		    }
		}
	}

    private int findEndOfStatement(Token[] tokens, int i)
    {
        return findNextOccuranceOfToken(tokens, i, TokenType.Semicolon);
    }

    private int findEndOfHeader(Token[] tokens, int i)
    {
		return findNextOccuranceOfToken(tokens, i, TokenType.OpeningBrace);
    }

    private int findNextOccuranceOfToken(Token[] tokens, int i, TokenType tokenType)
    {
        try
        {
            while (tokens[i].tokenType != tokenType)
            {
                i++;
            }
            i++;
            return i;
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

	private int parseDefinitionDeclaration(Token[] tokens,
                                           ArrayDeque<ContainingNode> nestingStatus,
                                           RootNode rootNode,
                                           int i) throws JPLException
    {
        var definitionNode = new DefinitionNode();
        rootNode.definitions.add(definitionNode);
        nestingStatus.push(definitionNode);
        i++;
        if (tokens[i].tokenType != TokenType.Identifier)
        {
            throwParserException(rootNode,
                                 "Definition must have a name.");
        }
        definitionNode.definitionName = tokens[i].tokenValue;
        i++;
        if (tokens[i].tokenType != TokenType.OpeningParenthesis)
        {
            throwParserException(rootNode,
                                 "Definition must have an opening parenthesis after name.");
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
            throwParserException(rootNode,
                                 "Opening brace required after function arguments.");
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
                throwParserException(rootNode,
                                     "arguments must have a type.");
            }
            argumentNode.type = JPLType.Integer;
            i++;
            if (tokens[i].tokenType != TokenType.Identifier)
            {
                throwParserException(rootNode,
                                     "arguments must have a identifier after their type.");
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
                throwParserException(rootNode,
                                     "Invalid extra token after argument.");
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
        for (T t : arrayList)
        {
            System.out.println(t);
        }
        return arrayList.get(getLastElementIndex(arrayList));
    }
}
