package com.jpl.fyp.compilerComponent;

import java.util.ArrayList;
import java.util.List;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.JPLType;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.classLibrary.nodes.ArgumentNode;
import com.jpl.fyp.classLibrary.nodes.ContainingNode;
import com.jpl.fyp.classLibrary.nodes.DefinitionNode;
import com.jpl.fyp.classLibrary.nodes.RootNode;
import com.jpl.fyp.classLibrary.nodes.StatementNode;

public class Parser
{
    public Parser(List<Token> tokenList) throws JPLException
    {
        parse(tokenList);
    }

	private void parse(List<Token> tokenList) throws JPLException
    {
        List<ContainingNode> nestingStatus = new ArrayList<ContainingNode>();
        // List< symbolTable = new List<(int, string, object)>();
        RootNode rootNode = new RootNode();

        for (int i = 0; i < tokenList.size(); i++)
        {
            if (tokenList.get(i).getTokenType() == TokenType.Define)
            {
                i = parseDefinition(tokenList, nestingStatus, rootNode, i);
            }
            else
            {
                throwParserException(rootNode, "all code must be contained within definitions.");
            }
        }

        System.out.println(rootNode);
	}

	private void throwParserException(RootNode rootNode,
                                      String message) throws JPLException
    {
        System.out.println(rootNode);
        throw new JPLException(message);
	}

	private int parseDefinition(List<Token> tokenList,
                                List<ContainingNode> nestingStatus,
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
            i = parseDefinitionDeclaration(tokenList,
                                           nestingStatus,
                                           rootNode,
                                           i);

            i = parseStatements(tokenList,
                                nestingStatus,
                                rootNode,
                                i);
        }
		return i;
	}

	private int parseStatements(List<Token> tokenList,
                                List<ContainingNode> nestingStatus,
                                RootNode rootNode,
                                int i) throws JPLException
    {
        while (true)
        {
            if (tokenList.get(i).getTokenType() == TokenType.ClosingBrace)
            {
                nestingStatus.remove(nestingStatus.size() - 1);
                if (nestingStatus.size() <= 0)
                {
                    break;
                }
            }
            else if (tokenList.get(i).getTokenType() == TokenType.While)
            {
                i = parseWhileDeclaration(tokenList, nestingStatus, rootNode, i);
            }
            else if (tokenList.get(i).getTokenType() == TokenType.IntegerDeclaration)
            {
                // this section needs to be expanded in future to handle
                // other data types. for now we will just have integers.
                // in the nestingstatus variable, include a reference to
                // the node with it. then you can add statements to
                // that reference declaration.
                i = parseDeclarationStatement(tokenList, nestingStatus, rootNode, i);
            }
            else if (tokenList.get(i).getTokenType() == TokenType.Identifier)
            {
                i++;
                // handle non-declarative assignments and standalone method calls
                if (tokenList.get(i).getTokenType() == TokenType.Assignment)
                {
                    i = parseAssignmentStatement(tokenList, nestingStatus, i);
                }
                else if (tokenList.get(i).getTokenType() == TokenType.OpeningParenthesis)
                {
                    i = parseStandaloneMethodCallStatement(tokenList, nestingStatus, rootNode, i);
                }
                else
                {
                    throwParserException(rootNode,
                                         "Invalid token, was expecting either an opening brace token " +
                                         "in case of a standalone method call or an assignment " +
                                         "token in case of variable assignment.");
                }
            }
            else if (tokenList.get(i).getTokenType() == TokenType.If)
            {
                IfNode ifNode = new IfNode();
                nestingStatus.get(nestingStatus.size() - 1).statements.add(ifNode);
                nestingStatus.add(ifNode);
                i++;
                i = parseIfStatement(tokenList,
                                     nestingStatus,
                                     rootNode,
                                     i);
            }
            else if (tokenList.get(i).getTokenType() == TokenType.Else)
            {
                // i should implement a sort of chain, starting with the initial if node.
                // a property of the if node should be an if or an else node,
                // which can continue to chain until an else is reached.

                // if node should have a property for a following if node,
                // this property would be populated if there is an else
                // statement directly after the if statement.

                StatementNode previousStatementNode = nestingStatus.get(nestingStatus.size() - 1).getStatements().get(nestingStatus.get(nestingStatus.size() - 1).getStatements().size() - 1);
                if (previousStatementNode.getClass() != IfNode)
                {
                    throwParserException(rootNode,
                                         "else statement can only occur after an if or else if statement.");
                }
                IfNode parentIfNode = (IfNode)previousStatementNode;
                i++;
                while (parentIfNode.elseStatement != null)
                {
                    // TODO: Add a check here to check for rouge else nodes.
                    parentIfNode = (IfNode)parentIfNode.elseStatement;
                }
                if (tokenList.get(i).getTokenType() == TokenType.If)
                {
                    // do else if stuff here
                    IfNode elseIfNode = new IfNode();
                    parentIfNode.elseStatement(tokenList,
                                               nestingStatus,
                                               rootNode,
                                               elseIfNode,
                                               i);
                }
                else if (tokenList.get(i).getTokenType() == TokenType.OpeningBrace)
                {
                    // do else stuff here
                    ElseNode elseNode = new ElseNode();
                    parentIfNode.elseStatement;
                }
                else
                {
                    ThrowParserException(rootNode,
                                         "else token must be followed by if " +
                                         "token incase of else if statement or opening paren " +
                                         "in the case of a straight else statement.");
                    
                }
                throwParserException(rootNode, "not implemented.");
            }
            i++;
        }
        
		return i;
	}

	private int parseDeclarationStatement(List<Token> tokenList,
                                          List<ContainingNode> nestingStatus,
                                          RootNode rootNode,
                                          int i)
    {
        DeclarationNode declarationNode = new DeclarationNode();
        nestingStatus.get(nestingStatus.size() - 1).statements.add(declarationNode);
        declarationNode.Type = JPLType.Integer;
        i++;
        if (tokenList.get(i).getTokenType() != TokenType.Identifier)
        {
            throwParserException(rootNode,
                                 "Missing variable name.");
        }
        // this is where you got to 11/11/20
		return i;
	}

	private int parseDefinitionDeclaration(List<Token> tokenList,
                                           List<ContainingNode> nestingStatus,
                                           RootNode rootNode,
                                           int i) throws JPLException
    {
        DefinitionNode definitionNode = new DefinitionNode();
        rootNode.definitions.add(definitionNode);
        nestingStatus.add(definitionNode);
        i++;
        if (tokenList.get(i).getTokenType() != TokenType.Identifier)
        {
            throwParserException(rootNode,
                                 "Definition must have a name.");
        }
        definitionNode.setDefinitionName(tokenList.get(i).getTokenValue());
        i++;
        if (tokenList.get(i).getTokenType() != TokenType.OpeningParenthesis)
        {
            throwParserException(rootNode,
                                 "Definition must have an opening parenthesis after name.");
        }
        i++;
        if (tokenList.get(i).getTokenType() == TokenType.ClosingParenthesis)
        {
            i++;
        }
        else
        {
            i = parseDefinitionArguments(tokenList,
                                         rootNode,
                                         i,
                                         definitionNode);
        }
        if (tokenList.get(i).getTokenType() != TokenType.OpeningBrace)
        {
            throwParserException(rootNode,
                                 "Opening brace required after function arguments.");
        }
        i++;
		return i;
	}

	private int parseDefinitionArguments(List<Token> tokenList,
                                         RootNode rootNode,
                                         int i,
                                         DefinitionNode definitionNode) throws JPLException
    {
        while (true)
        {
            definitionNode.getArguments().add(new ArgumentNode());
            ArgumentNode argumentNode = definitionNode.getArguments().get(definitionNode.getArguments().size()-1);
            // this if needs to include all other declaration tokens if/when they are added.
            if (tokenList.get(i).getTokenType() != TokenType.IntegerDeclaration)
            {
                throwParserException(rootNode,
                                     "arguments must have a type.");
            }
            argumentNode.type = JPLType.Integer;
            i++;
            if (tokenList.get(i).getTokenType() != TokenType.Identifier)
            {
                throwParserException(rootNode,
                                     "arguments must have a identifier after their type.");
            }

            argumentNode.identifier = tokenList.get(i).getTokenValue();
            i++;
            if (tokenList.get(i).getTokenType() == TokenType.ClosingParenthesis)
            {
                i++;
                break;
            }
            else if (tokenList.get(i).getTokenType() == TokenType.Comma)
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

	private boolean containsDefinitionNode(List<ContainingNode> nestingStatus)
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
}
