package com.jpl.fyp.compilerComponent;

import java.util.ArrayList;
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
import com.jpl.fyp.classLibrary.nodes.IfNode;
import com.jpl.fyp.classLibrary.nodes.FunctionCallNode;
import com.jpl.fyp.classLibrary.nodes.RootNode;
import com.jpl.fyp.classLibrary.nodes.StatementNode;
import com.jpl.fyp.classLibrary.nodes.WhileNode;

public class Parser
{
    public Parser(List<Token> tokenList) throws JPLException
    {
        parse(tokenList);
    }

	private void parse(List<Token> tokenList) throws JPLException
    {
        var nestingStatus = new ArrayList<ContainingNode>();
        // List< symbolTable = new List<(int, string, object)>();
        var rootNode = new RootNode();

        for (int i = 0; i < tokenList.size(); i++)
        {
            if (tokenList.get(i).getTokenType() == TokenType.Define)
            {
                i = parseDefinition(tokenList, nestingStatus, rootNode, i);
            }
            else
            {
                throwParserException(rootNode,
                                     "all code must be contained within definitions.");
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
                nestingStatus.remove(getLastElementIndex(nestingStatus));
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
                    i = parseStandaloneMethodCallStatement(tokenList,
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
            else if (tokenList.get(i).getTokenType() == TokenType.If)
            {
                IfNode ifNode = new IfNode();
                getLastElement(nestingStatus).statements.add(ifNode);
                nestingStatus.add(ifNode);
                i++;
                i = parseIfStatement(tokenList,
                                     nestingStatus,
                                     rootNode,
                                     ifNode,
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

                StatementNode previousStatementNode = getLastElement(getLastElement(nestingStatus).statements);
                if (!(previousStatementNode instanceof IfNode))
                {
                    throwParserException(rootNode,
                                         "else statement can only occur after an if or else if statement.");
                }
                i++;
                var parentIfNode = (IfNode)previousStatementNode;
                parentIfNode = getLastOfIfElseChain(parentIfNode);

                if (tokenList.get(i).getTokenType() == TokenType.If)
                {
                    // do else if stuff here
                    var elseIfNode = new IfNode();
                    parentIfNode.elseNode = elseIfNode;
                    getLastElement(nestingStatus).statements.add(elseIfNode);
                    nestingStatus.add(elseIfNode);
                    i++;
                    i = parseIfStatement(tokenList,
                                         nestingStatus,
                                         rootNode,
                                         elseIfNode,
                                         i);
                }
                else if (tokenList.get(i).getTokenType() == TokenType.OpeningBrace)
                {
                    // do else stuff here
                    var elseNode = new ContainingNode();
                    parentIfNode.elseNode = elseNode;
                    i++;
                }
                else
                {
                    throwParserException(rootNode,
                                         "else token must be followed by if " +
                                         "token incase of else if statement or opening paren " +
                                         "in the case of a straight else statement.");
                    
                }
                // throwParserException(rootNode, "not implemented.");
            }
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

	private int parseIfStatement(List<Token> tokenList,
                                 List<ContainingNode> nestingStatus,
                                 RootNode rootNode,
                                 IfNode ifNode,
                                 int i) throws JPLException
    {
        if (tokenList.get(i).getTokenType() != TokenType.OpeningParenthesis)
        {
            throwParserException(rootNode,
                                 "Opening parenthesis expected after if token.");
        }
        i++;
        var expressionTokens = new ArrayList<Token>();
        ifNode.testExpression.expressionTokens = expressionTokens;
        
        while (tokenList.get(i).getTokenType() != TokenType.ClosingParenthesis)
        {
            expressionTokens.add(tokenList.get(i));
            i++;
        }
        i++;
        if (tokenList.get(i).getTokenType() != TokenType.OpeningBrace)
        {
            throwParserException(rootNode,
                                 "Opening brace expected after expression of if statement.");
        }
        
		return i;
	}

	private int parseStandaloneMethodCallStatement(List<Token> tokenList,
                                                   List<ContainingNode> nestingStatus,
                                                   RootNode rootNode,
                                                   int i) throws JPLException
    {
        FunctionCallNode functionCallNode = new FunctionCallNode();
        // TODO: this code is cursed, watch out for this later
        getLastElement(nestingStatus).statements.add(functionCallNode);
        
        functionCallNode.identifier = tokenList.get(i - 1).getTokenValue();
        i++;
        
        if (tokenList.get(i).getTokenType() != TokenType.ClosingParenthesis)
        {
            // start reading args.
            // each arg will take the form of an expressionNode.
            while (true)
            {
                // create new argument
                var expressionNode = new ExpressionNode();
                functionCallNode.arguments.add(expressionNode);
                var expressionTokens = new ArrayList<Token>();

                while (tokenList.get(i).getTokenType() != TokenType.ClosingParenthesis
                       &&
                       tokenList.get(i).getTokenType() != TokenType.Comma)
                {
                    // populate argument
                    expressionTokens.add(tokenList.get(i));
                    i++;
                }
                expressionNode.expressionTokens = expressionTokens;
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
            }
            if (tokenList.get(i).getTokenType() != TokenType.Semicolon)
            {
                throwParserException(rootNode,
                                     "Semi colon expected after standalone method call.");
            }
            i++;
        }
        return i;
	}

	private int parseAssignmentStatement(List<Token> tokenList,
                                         List<ContainingNode> nestingStatus,
                                         int i)
    {
        var assignmentNode = new AssignmentNode();
        getLastElement(nestingStatus).statements.add(assignmentNode);
        assignmentNode.assignmentTarget = tokenList.get(i - 1).getTokenValue();
        i++;
        assignmentNode.expression = new ExpressionNode();
        var expressionTokens = new ArrayList<Token>();
        while (tokenList.get(i).getTokenType() != TokenType.Semicolon)
        {
            expressionTokens.add(tokenList.get(i));
            i++;
        }
        assignmentNode.expression.expressionTokens = expressionTokens;
		return i;
	}

	private int parseWhileDeclaration(List<Token> tokenList,
                                      List<ContainingNode> nestingStatus,
                                      RootNode rootNode,
                                      int i) throws JPLException
    {
        var whileNode = new WhileNode();
        getLastElement(nestingStatus).statements.add(whileNode);
        nestingStatus.add(whileNode);
        i++;
        if (tokenList.get(i).getTokenType() != TokenType.OpeningParenthesis)
        {
            throwParserException(rootNode,
                                 "Parenthesis at start of expression expected.");
        }
        i++;
        whileNode.testExpression = new ExpressionNode();
        List<Token> expressionTokens = new ArrayList<Token>();
        while (tokenList.get(i).getTokenType() != TokenType.OpeningBrace)
        {
            expressionTokens.add(tokenList.get(i));
            i++;
        }
        if (getLastElement(expressionTokens).getTokenType() != TokenType.ClosingParenthesis)
        {
            throwParserException(rootNode,
                                 "While test expression must have closing bracket.");
        }
        expressionTokens.remove(getLastElementIndex(expressionTokens));
        whileNode.testExpression.expressionTokens = expressionTokens;
		return i;
	}

	private int parseDeclarationStatement(List<Token> tokenList,
                                          List<ContainingNode> nestingStatus,
                                          RootNode rootNode,
                                          int i) throws JPLException
    {
        var declarationNode = new DeclarationNode();
        getLastElement(nestingStatus).statements.add(declarationNode);
        declarationNode.type = JPLType.Integer;
        i++;
        if (tokenList.get(i).getTokenType() != TokenType.Identifier)
        {
            throwParserException(rootNode,
                                 "Missing variable name.");
        }
        declarationNode.identifier = tokenList.get(i).getTokenValue();
        i++;
        if (tokenList.get(i).getTokenType() == TokenType.Assignment)
        {
            i++;
            declarationNode.expression = new ExpressionNode();
            var expressionTokens = new ArrayList<Token>();

            while (tokenList.get(i).getTokenType() != TokenType.Semicolon)
            {
                expressionTokens.add(tokenList.get(i));
                i++;
            }
            declarationNode.expression.expressionTokens = expressionTokens;
        }
        else if (tokenList.get(i).getTokenType() == TokenType.Semicolon)
        {
            // i++;
            // TODO: review whether this should be removed, as it doesnt do shit
        }
        else
        {
            throwParserException(rootNode,
                                 "If something is being assigned, assignment token is required. If declaring then a semi colon token is required.");
        }
        
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
            var argumentNode = new ArgumentNode();
            definitionNode.getArguments().add(argumentNode);
            
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

    private <T> int getLastElementIndex(List<T> arrayList)
    {
        return arrayList.size() - 1;
    }

    private <T> T getLastElement(List<T> arrayList)
    {
        return arrayList.get(getLastElementIndex(arrayList));
    }
}
