package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;

public class RootNode extends ContainingNode {
    
    private ArrayDeque<ContainingNode> nestingStatus;

    public RootNode() {
        this.nestingStatus = new ArrayDeque<ContainingNode>();
        this.nestingStatus.add(this);
    }

	public void addNode(StatementNode node) throws JPLException {
        this.throwExceptionIfStatementOutsideOfDefinition(node);

        if (node instanceof DefinitionNode) {
            addDefinitionNodeToDefinitions(node);
        } else if (node instanceof ElseNode || node instanceof ElseIfNode) {
            this.addElseOrElseIfNodeToCurrentIfStatement(node);
        } else {
            this.addGenericStatementNodeToCurrentContainingStatement(node);
        }

        this.updateSymbolTable(node);

        if (node instanceof ContainingNode) {
            enterNewContainingNode(node);
        }
	}

    public void updateSymbolTable(StatementNode node) {
        SymbolTableEntry nodeSymbolTableEntry = node.getSymbolTableEntry();
        if (nodeSymbolTableEntry != null) {
            getCurrentContainingNode().addSymbolToTable(nodeSymbolTableEntry);
        }
	}

    private ContainingNode getCurrentContainingNode() {
        return nestingStatus.peek();
    }

    public ArrayDeque<ContainingNode> getNestingStatus() {
		return nestingStatus;
	}

	public void setNestingStatus(ArrayDeque<ContainingNode> nestingStatus) {
		this.nestingStatus = nestingStatus;
	}

	private void enterNewContainingNode(StatementNode node) {
		nestingStatus.push((ContainingNode)node);
	}

	private void addGenericStatementNodeToCurrentContainingStatement(StatementNode node) {
		getCurrentContainingNode().addStatement(node);
	}

	private void addElseOrElseIfNodeToCurrentIfStatement(StatementNode node) throws JPLException {
		StatementNode previousStatementNode = getPreviousStatementNode();
		if (!(previousStatementNode instanceof IfNode)) {
		    throw new JPLException("else statement can only occur after an if or else if statement.");
		}
		var parentIfNode = (ConditionalNode)previousStatementNode;
		parentIfNode = getLastOfIfElseChain(parentIfNode);
		parentIfNode.setElseNode((ContainingNode)node);
	}

	private StatementNode getPreviousStatementNode() {
		return getLastElement(getCurrentContainingNode().getStatements());
	}

	private void addDefinitionNodeToDefinitions(StatementNode node) throws JPLException {
		if (containsDefinitionNode(nestingStatus))
		{
		    throw new JPLException("cannot define function inside of function.");
		}
		super.addStatement(node);
	}

	private void throwExceptionIfStatementOutsideOfDefinition(StatementNode node) throws JPLException {
		if (!(node instanceof DefinitionNode)
            &&
            !(containsDefinitionNode(nestingStatus)))
        {
            throw new JPLException("all code must be contained within definitions.");
        }
	}

    private boolean containsDefinitionNode(ArrayDeque<ContainingNode> nestingStatus) {
        for (ContainingNode containingNode : nestingStatus) {
        	if (containingNode instanceof DefinitionNode) {
                return true;
            }
        }
		return false;
	}

    private ConditionalNode getLastOfIfElseChain(ConditionalNode parentIfNode) {
        while (parentIfNode.getElseNode() != null) {
            // TODO: Add a check here to check for rouge else nodes.
            parentIfNode = (IfNode)parentIfNode.getElseNode();
        }
        return parentIfNode;
	}

    private <T> int getLastElementIndex(List<T> arrayList) {
        return arrayList.size() - 1;
    }

    private <T> T getLastElement(List<T> arrayList) {
        return arrayList.get(getLastElementIndex(arrayList));
    }

    @Override
    public String toString() {
        return "Root Node:\n"
            + super.toString() + "\n";
    }

	public void beginValidation() throws JPLException {
        SymbolTableEntry[] entries = {};
        this.validate(entries);
	}

	public IntermediateCodeInstruction[] beginIntermediateCodeGeneration() throws JPLException {
		return this.generateIntermediateCode().toArray(new IntermediateCodeInstruction[0]);
	}

    @Override
    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        var instructions = new ArrayList<IntermediateCodeInstruction>();
        for (StatementNode definitionNode : this.getStatements()) {
            instructions.addAll(definitionNode.generateIntermediateCode());
        }
		return instructions;
    }

	public SymbolTableEntry[] getAllVariableSymbols() {
        var symbols = new ArrayList<SymbolTableEntry>();
        for (StatementNode statement : this.getStatements()) {
            symbols.addAll(statement.getAllSymbols());
        }
		return symbols.toArray(new SymbolTableEntry[0]);
	}

	public String[] getAllParameterNames() {
        var names = new ArrayList<String>();
        for (StatementNode statement : this.getStatements()) {
            names.addAll(((DefinitionNode) statement).getArgumentNames());
        }
		return names.toArray(new String[0]);
	}
}
