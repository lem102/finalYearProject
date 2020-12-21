package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayDeque;

import com.jpl.fyp.classLibrary.JPLException;

public interface Node
{
    int moveIndexToNextStatement(int endOfStatement, int endOfHeader);

    ArrayDeque<ContainingNode> updateNestingStatus(ArrayDeque<ContainingNode> nestingStatus) throws JPLException;
}
