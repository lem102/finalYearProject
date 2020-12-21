package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.JPLException;

public interface Node
{
    int moveIndexToNextStatement(int endOfStatement, int endOfHeader);

    RootNode addToRootNode(RootNode rootNode) throws JPLException;
}
