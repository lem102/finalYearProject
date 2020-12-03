package com.jpl.fyp.classLibrary.nodes;

import java.util.List;

public interface ContainingNode extends StatementNode
{
    List<StatementNode> getStatements();

    void addStatement(StatementNode node);
}
