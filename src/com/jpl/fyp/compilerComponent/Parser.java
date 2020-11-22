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
import com.jpl.fyp.classLibrary.nodes.ContainingNode;
import com.jpl.fyp.classLibrary.nodes.DeclarationNode;
import com.jpl.fyp.classLibrary.nodes.DefinitionNode;
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
            }
            else if (tokens[i].tokenType == TokenType.While)
            {
                int endOfWhileHeader = findEndOfWhileHeader(tokens,
                                                            i);
                Token[] relevantTokens = Arrays.copyOfRange(tokens,
                                                            i,
                                                            endOfWhileHeader);
                var whileNode = new WhileNode(relevantTokens, rootNode);
                nestingStatus.peek().statements.add(whileNode);
                nestingStatus.push(whileNode);
                i = endOfWhileHeader;
            }
            else if (tokens[i].tokenType == TokenType.IntegerDeclaration)
            {
                // this section needs to be expanded in future to handle
                // other data types. for now we will just have integers.
                // in the nestingstatus variable, include a reference to
                // the node with it. then you can add statements to
                // that reference declaration.
                
                var declarationNode = parseDeclarationStatement(tokens,
                                                                rootNode,
                                                                i);
                nestingStatus.peek().statements.add(declarationNode);

                i = findEndOfDeclarationStatement(tokens, i);
            }
            else if (tokens[i].tokenType == TokenType.Identifier)
            {
                i++;
                // handle non-declarative assignments and standalone method calls
                if (tokens[i].tokenType == TokenType.Assignment)
                {
                    var relevantTokens = Arrays.copyOfRange(tokens,
                                                            i-1,
                                                            findEndOfAssignmentStatement(tokens,
                                                                                         i));
                    var assignmentNode = new AssignmentNode(relevantTokens, rootNode);

                    nestingStatus.peek().statements.add(assignmentNode);
                    i = findEndOfAssignmentStatement(tokens, i);
                }
                else if (tokens[i].tokenType == TokenType.OpeningParenthesis)
                {
                    i = parseStandaloneMethodCallStatement(tokens,
                                                           nestingStatus,
                                                           rootNode,
                                                           i);
                }
                else
                {
                    throwParserException(rootNode,
                                         "Invalid token, was expecting either an opening brace token " +
                                         "in case of a standalone method call or an assignment " +
                                         "token in case of variable assignment.");
                }
            }
            else if (tokens[i].tokenType == TokenType.If)
            {
                IfNode ifNode = new IfNode();
                nestingStatus.peek().statements.add(ifNode);
                nestingStatus.push(ifNode);
                i++;
                i = parseIfStatement(tokens,
                                     nestingStatus,
                                     rootNode,
                                     ifNode,
                                     i);
            }
            else if (tokens[i].tokenType == TokenType.Else)
            {
                // i should implement a sort of chain, starting with the initial if node.
                // a property of the if node should be an if or an else node,
                // which can continue to chain until an else is reached.

                // if node should have a property for a following if node,
                // this property would be populated if there is an else
                // statement directly after the if statement.

                StatementNode previousStatementNode = getLastElement(nestingStatus.peek().statements);
                if (!(previousStatementNode instanceof IfNode))
                {
                    throwParserException(rootNode,
                                         "else statement can only occur after an if or else if statement.");
                }
                i++;
                var parentIfNode = (IfNode)previousStatementNode;
                parentIfNode = getLastOfIfElseChain(parentIfNode);

                if (tokens[i].tokenType == TokenType.If)
                {
                    // do else if stuff here
                    var elseIfNode = new IfNode();
                    parentIfNode.elseNode = elseIfNode;
                    nestingStatus.push(elseIfNode);
                    i++;
                    i = parseIfStatement(tokens,
                                         nestingStatus,
                                         rootNode,
                                         elseIfNode,
                                         i);
                }
                else if (tokens[i].tokenType == TokenType.OpeningBrace)
                {
                    // do else stuff here
                    var elseNode = new ContainingNode();
                    parentIfNode.elseNode = elseNode;
                    nestingStatus.push(elseNode);
                }
                else
                {
                    throwParserException(rootNode,
                                         "else token must be followed by if " +
                                         "token incase of else if statement or opening paren " +
                                         "in the case of a straight else statement.");
                    
                }
            }
            i++;
        }
        
		return i;
	}

	private int findEndOfAssignmentStatement(Token[] tokens,
                                             int i)
    {
        i++;
        while (tokens[i].tokenType != TokenType.Semicolon)
        {
            i++;
        }
		return i;
	}

	private int findEndOfDeclarationStatement(Token[] tokens,
                                              int i)
    {
        i++;
        i++;
        if (tokens[i].tokenType == TokenType.Assignment)
        {
            i++;
            while (tokens[i].tokenType != TokenType.Semicolon)
            {
                i++;
            }
        }
		return i;
	}

	private int findEndOfWhileHeader(Token[] tokens,
                                     int i)
    {
        while (tokens[i].tokenType != TokenType.OpeningBrace)
        {
            i++;
        }
		return i;
	}

	private IfNode getLastOfIfElseChain(IfNode parentIfNode)
    {
        while (parentIfNode.elseNode != null)
        {
            // TODO: Add a check here to check for rouge else nodes.
            parentIfNode = (IfNode)parentIfNode.elseNode;
        }
        return parentIfNode;
	}

	private int parseIfStatement(Token[] tokens,
                                 ArrayDeque<ContainingNode> nestingStatus,
                                 RootNode rootNode,
                                 IfNode ifNode,
                                 int i) throws JPLException
    {
        if (tokens[i].tokenType != TokenType.OpeningParenthesis)
        {
            throwParserException(rootNode,
                                 "Opening parenthesis expected after if token.");
        }
        i++;
        var expressionTokens = new ArrayList<Token>();
        ifNode.testExpression.expressionTokens = expressionTokens;
        
        while (tokens[i].tokenType != TokenType.ClosingParenthesis)
        {
            expressionTokens.add(tokens[i]);
            i++;
        }
        i++;
        if (tokens[i].tokenType != TokenType.OpeningBrace)
        {
            throwParserException(rootNode,
                                 "Opening brace expected after expression of if statement.");
        }
        
		return i;
	}

	private int parseStandaloneMethodCallStatement(Token[] tokens,
                                                   ArrayDeque<ContainingNode> nestingStatus,
                                                   RootNode rootNode,
                                                   int i) throws JPLException
    {
        var functionCallNode = new FunctionCallNode();
        // TODO: this code is cursed, watch out for this later
        // TODO: this code is REALLY cursed, do not look at this when you are sleepy.
        nestingStatus.peek().statements.add(functionCallNode);
        
        functionCallNode.identifier = tokens[i - 1].tokenValue;
        i++;

        if (tokens[i].tokenType != TokenType.ClosingParenthesis)
        {
            // start reading args.
            // each arg will take the form of an expressionNode.
            while (true)
            {
                // create new argument
                var expressionNode = new ExpressionNode();
                functionCallNode.arguments.add(expressionNode);
                var expressionTokens = new ArrayList<Token>();

                while (tokens[i].tokenType != TokenType.ClosingParenthesis
                       &&
                       tokens[i].tokenType != TokenType.Comma)
                {
                    // populate argument
                    expressionTokens.add(tokens[i]);
                    i++;
                }
                expressionNode.expressionTokens = expressionTokens;
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
            }
            if (tokens[i].tokenType != TokenType.Semicolon)
            {
                throwParserException(rootNode,
                                     "Semi colon expected after standalone method call.");
            }
        }
        return i;
	}

    private DeclarationNode parseDeclarationStatement(Token[] tokens,
                                                      RootNode rootNode,
                                                      int i) throws JPLException
    {
        var declarationNode = new DeclarationNode();
        declarationNode.type = JPLType.Integer;
        i++;
        if (tokens[i].tokenType != TokenType.Identifier)
        {
            throwParserException(rootNode,
                                 "Missing variable name.");
        }
        declarationNode.identifier = tokens[i].tokenValue;
        i++;
        if (tokens[i].tokenType == TokenType.Assignment)
        {
            i++;
            declarationNode.expression = new ExpressionNode();
            var expressionTokens = new ArrayList<Token>();

            while (tokens[i].tokenType != TokenType.Semicolon)
            {
                expressionTokens.add(tokens[i]);
                i++;
            }
            declarationNode.expression.expressionTokens = expressionTokens;
        }
        else if (tokens[i].tokenType == TokenType.Semicolon)
        {
            // i++;
            // TODO: review whether this should be removed, as it doesnt do shit
        }
        else
        {
            throwParserException(rootNode,
                                 "If something is being assigned, assignment token is required. If declaring then a semi colon token is required.");
        }
        
		return declarationNode;
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
        return arrayList.get(getLastElementIndex(arrayList));
    }
}
