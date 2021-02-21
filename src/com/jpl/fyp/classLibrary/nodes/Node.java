package com.jpl.fyp.classLibrary.nodes;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;

public interface Node
{
    int moveIndexToNextStatement(int endOfStatement, int endOfHeader);

    void validate(SymbolTableEntry[] entries) throws JPLException;

    IntermediateCodeInstruction[] generateItermediateCode(IntermediateCodeInstruction[] instructions);
}
