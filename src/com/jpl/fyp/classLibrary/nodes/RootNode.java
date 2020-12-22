package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayDeque;
import java.util.ArrayList;
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

    
	public void addNode(StatementNode node) throws JPLException
    {
        throwExceptionIfStatementOutsideOfDefinition(node);

        if (node instanceof DefinitionNode)
        {
            addDefinitionNodeToDefinitions(node);
        }
        else if (node instanceof ElseNode
                 ||
                 node instanceof ElseIfNode)
        {
            addElseOrElseIfNodeToCurrentIfStatement(node);
        }
        else
        {
            addGenericStatementNodeToCurrentContainingStatement(node);
        }

        if (node instanceof ContainingNode)
        {
            enterNewContainingNode(node);
        }
	}


    private ContainingNode getCurrentContainingNode()
    {
        return nestingStatus.peek();
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

	private void enterNewContainingNode(StatementNode node)
    {
		nestingStatus.push((ContainingNode)node);
	}

	private void addGenericStatementNodeToCurrentContainingStatement(StatementNode node)
    {
		getCurrentContainingNode().addStatement(node);
	}

	private void addElseOrElseIfNodeToCurrentIfStatement(StatementNode node) throws JPLException
    {
		StatementNode previousStatementNode = getPreviousStatementNode();
		if (!(previousStatementNode instanceof IfNode))
		{
		    throw new JPLException("else statement can only occur after an if or else if statement.");
		}
		var parentIfNode = (ConditionalNode)previousStatementNode;
		parentIfNode = getLastOfIfElseChain(parentIfNode);
		parentIfNode.setElseNode((ContainingNode)node);
	}

	private StatementNode getPreviousStatementNode()
    {
		return getLastElement(getCurrentContainingNode().getStatements());
	}

	private void addDefinitionNodeToDefinitions(StatementNode node) throws JPLException
    {
		if (containsDefinitionNode(nestingStatus))
		{
		    throw new JPLException("cannot define function inside of function.");
		}
		definitions.add((DefinitionNode)node);
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

    private ConditionalNode getLastOfIfElseChain(ConditionalNode parentIfNode)
    {
        while (parentIfNode.getElseNode() != null)
        {
            // TODO: Add a check here to check for rouge else nodes.
            parentIfNode = (IfNode)parentIfNode.getElseNode();
        }
        return parentIfNode;
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
