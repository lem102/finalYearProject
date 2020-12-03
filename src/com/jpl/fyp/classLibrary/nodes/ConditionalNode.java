package com.jpl.fyp.classLibrary.nodes;

public interface ConditionalNode extends ContainingNode
{
    ContainingNode getElseNode();

    void setElseNode(ContainingNode elseNode);
}
