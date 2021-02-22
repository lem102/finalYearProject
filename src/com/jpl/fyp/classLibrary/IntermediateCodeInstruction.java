package com.jpl.fyp.classLibrary;

public class IntermediateCodeInstruction {
	// class is based off the quadruple concept from dragon book page 366
    // TODO: usage of string as type is temporary, should refer to symbol table or something similar.
    private IntermediateCodeInstructionType operator;
    private String argument1;
    private String argument2;
    private String result;

    public IntermediateCodeInstruction(IntermediateCodeInstructionType operator) {
        this(operator, "");
	}

    public IntermediateCodeInstruction(IntermediateCodeInstructionType operator, String argument1) {
        this(operator, "", "");
	}

	public IntermediateCodeInstruction(IntermediateCodeInstructionType operator, String argument1, String argument2) {
        this(operator, "", "", "");
	}

	public IntermediateCodeInstruction(IntermediateCodeInstructionType operator, String argument1, String argument2, String result) {
        this.operator = operator;
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.result = result;
	}

	@Override
    public String toString() {
        return operator.toString() + ;
    }
}
