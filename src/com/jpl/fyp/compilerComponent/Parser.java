package com.jpl.fyp.compilerComponent;

import java.util.ArrayList;
import java.util.List;

import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.classLibrary.nodes.ContainingNode;
import com.jpl.fyp.classLibrary.nodes.RootNode;

public class Parser
{
    public Parser(List<Token> tokenList)
    {
        parse(tokenList);
    }

	private void parse(List<Token> tokenList)
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

	private void throwParserException(RootNode rootNode, String string)
    {
        
	}

	private int parseDefinition(List<Token> tokenList,
                                List<ContainingNode> nestingStatus,
                                RootNode rootNode,
                                int i)
    {
		return i;
	}
}
