package com.jpl.fyp.classLibrary;

public class IntermediateCodeInstruction {
	// class is based off the quadruple concept from dragon book page 366
    private static int temporaryVariableIndex = 0;
    private static int labelIndex = 0;

	public static int getCurrentTemporaryVariableIndex() {
		return temporaryVariableIndex;
	}
    
    public static String getNewTemporaryVariableName() {
        int currentIndex = temporaryVariableIndex;
        temporaryVariableIndex++;
        return "t" + currentIndex;
    }

    public static String getNewLabelName() {
        int currentIndex = labelIndex;
        labelIndex++;
        return "L" + currentIndex;
	}

    private IntermediateCodeInstructionType operator;
    private String argument1;
    private String argument2;
    private String result;

    public IntermediateCodeInstruction(IntermediateCodeInstructionType operator,
                                       String argument1,
                                       String argument2,
                                       String result) {
        this.operator = operator;
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.result = result;
	}

    public String getResult() {
        return this.result;
    }

	@Override
    public String toString() {
        return String.format("TAC Instruction: {%-15s %-15s %-15s %-15s}",
                             operator.toString(),
                             argument1,
                             argument2,
                             result);
    }

	public static String IntegerToInstructionName(int integer) {
		return "t" + integer;
	}
}
