package com.jpl.fyp.classLibrary.nodes;

public class ConditionalNode extends ContainingNode {
    private ContainingNode elseNode;
    
	public ContainingNode getElseNode() {
		return this.elseNode;
	}

	public void setElseNode(ContainingNode elseNode) {
		this.elseNode = elseNode;
	}
}
