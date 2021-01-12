package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayDeque;
import java.util.List;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;

public class RootNode extends ContainingNode
{
    private ArrayDeque<ContainingNode> nestingStatus;

    public RootNode()
    {
        this.nestingStatus = new ArrayDeque<ContainingNode>();
        this.nestingStatus.add(this);
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

        updateSymbolTable(node);

        if (node instanceof ContainingNode)
        {
            enterNewContainingNode(node);
        }
	}

    public void updateSymbolTable(StatementNode node) {
        SymbolTableEntry nodeSymbolTableEntry = node.getSymbolTableEntry();
        if (nodeSymbolTableEntry != null) {
            getCurrentContainingNode().addSymbolToTable(nodeSymbolTableEntry);
        }
	}


    private ContainingNode getCurrentContainingNode()
    {
        
        return nestingStatus.peek();
    }

    @Override
    public String toString()
    {
        return "Root Node:\n"
            + super.toString() + "\n";
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
		super.addStatement(node);
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

    private <T> T getLastElement(List<T> arrayList) {
        return arrayList.get(getLastElementIndex(arrayList));
    }
}
