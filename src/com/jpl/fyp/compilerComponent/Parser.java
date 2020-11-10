package com.jpl.fyp.compilerComponent;

import java.util.ArrayList;
import java.util.List;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.classLibrary.nodes.ArgumentNode;
import com.jpl.fyp.classLibrary.nodes.ContainingNode;
import com.jpl.fyp.classLibrary.nodes.DefinitionNode;
import com.jpl.fyp.classLibrary.nodes.RootNode;

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
            i = parseDefinitionDeclaration(tokenList,
                                           nestingStatus,
                                           rootNode,
                                           i);

            i = parseStatements(tokenList,
                                nestingStatus,
                                rootNode,
                                i);
        }
        else
        {
            throwParserException(rootNode,
                                 "cannot define function inside of function.");
        }
		return i;
	}

	private int parseStatements(List<Token> tokenList,
                                List<ContainingNode> nestingStatus,
                                RootNode rootNode,
                                int i) throws JPLException
    {
        throwParserException(rootNode, "not implemented.");
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
