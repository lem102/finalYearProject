package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.jpl.fyp.classLibrary.JPLException;

public class RootNode
{
    private List<DefinitionNode> definitions;

    private ArrayDeque<ContainingNode> nestingStatus;

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

    public ArrayDeque<ContainingNode> getNestingStatus()
    {
		return nestingStatus;
	}

	public void setNestingStatus(ArrayDeque<ContainingNode> nestingStatus)
    {
		this.nestingStatus = nestingStatus;
	}

	public void addNode(StatementNode node) throws JPLException
    {
        throwExceptionIfStatementOutsideOfDefinition(node);

        node.addToRootNode(this);

        // next incorporate the below if statement

        // if (node instanceof ContainingNode)
        // {
        //     nestingStatus.push((ContainingNode)node);
        // }
	}

	private void throwExceptionIfStatementOutsideOfDefinition(StatementNode node)
        throws JPLException
    {
		if (!(node instanceof DefinitionNode)
            &&
            !(containsDefinitionNode(nestingStatus)))
        {
            throw new JPLException("all code must be contained within definitions.");
        }
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
}
