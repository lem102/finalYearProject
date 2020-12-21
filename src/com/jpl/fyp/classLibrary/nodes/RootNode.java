package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class RootNode
{
    private List<DefinitionNode> definitions;

    private Deque<ContainingNode> nestingStatus;

    public RootNode()
    {
        this.definitions = new ArrayList<DefinitionNode>();
        this.nestingStatus = new ArrayDeque<ContainingNode>();
    }

    @Override
    public String toString()
    {
        String output = "";
        
        for (DefinitionNode definitionNode : definitions)
        {
            output += definitionNode;
        }
        
        return output;
    }

	public List<DefinitionNode> getDefinitions()
    {
		return definitions;
	}

	public void setDefinitions(List<DefinitionNode> definitions)
    {
		this.definitions = definitions;
	}

    public static String test()
    {
        return "test";
    }
}
