package com.jpl.fyp.compilerComponent;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.nodes.RootNode;

public class IntermediateCodeGenerator {
	public static IntermediateCodeInstruction[] generateIntermediateCode(RootNode syntaxTree) throws JPLException {
		return syntaxTree.beginIntermediateCodeGeneration();
	}
}
