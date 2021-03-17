package com.jpl.fyp.compilerComponent;

import java.util.ArrayList;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;

public class IntermediateCodeTranslator {
	public static ArrayList<String> translateIntermediateCodeIntoAssembly(IntermediateCodeInstruction[] intermediateCode) {
        var lines = new ArrayList<String>();
        for (IntermediateCodeInstruction instruction : intermediateCode) {
            lines.addAll(translateInstructionIntoAssembly(instruction));
        }
        return lines;
	}

	private static ArrayList<String> translateInstructionIntoAssembly(IntermediateCodeInstruction instruction) {
        var lines = new ArrayList<String>();
        switch (instruction.getOperator()) {
            case Label: {
                lines.add(instruction.getArgument1() + ":");
                break;
            }
            case Declare: {
                lines.addAll(translateDeclare(instruction));
                break;
            }
            case Print: {
                lines.addAll(translatePrint(instruction));
                break;
            }
            case Add: {
                lines.addAll(translateSummation(instruction, "add"));
                break;
            }
            case Subtract: {
                lines.addAll(translateSummation(instruction, "sub"));
                break;
            }
            case Multiply: {
                lines.addAll(translateMultiplication(instruction, "mul"));
                break;
            }
            case Divide: {
                lines.addAll(translateMultiplication(instruction, "div"));
                break;
            }
            default: {
                lines.add("; instruction untranslated");
                break;
            }
        }
        return lines;
	}

	private static ArrayList<String> translatePrint(IntermediateCodeInstruction instruction) {
        String firstLine;
        if (isInteger(instruction.getResult())) {
            firstLine = "mov eax, " + instruction.getResult();
        } else {
            firstLine = "mov eax, [" + instruction.getResult() + "]";
        }
        var secondLine = "call iprintLF ";
        var lines = new ArrayList<String>();
        lines.add(firstLine);
        lines.add(secondLine);
        return lines;
	}

	private static ArrayList<String> translateDeclare(IntermediateCodeInstruction instruction) {
        String instructionResult = instruction.getResult();
        String instructionArgument = instruction.getArgument1();
        if (instructionArgument == null) {
            instructionArgument = "0";
        }

        String firstLine = operationBuilder("mov", "eax", addBracketsIfVariable(instructionArgument));
        String secondLine = operationBuilder("mov", addBracketsIfVariable(instructionResult), "eax");

        var lines = new ArrayList<String>();
        lines.add(firstLine);
        lines.add(secondLine);
		return lines;
	}

	private static boolean isInteger(String string) {
		try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
	}

	private static ArrayList<String> translateSummation(IntermediateCodeInstruction instruction, String operation) {
        String firstArgument = instruction.getArgument1();
        String secondArgument = instruction.getArgument2();
        String result = instruction.getResult();

        String firstLine = operationBuilder("mov", "eax", addBracketsIfVariable(firstArgument));
        String secondLine = operationBuilder("mov", "ebx", addBracketsIfVariable(secondArgument));
        String thirdLine = operationBuilder(operation, "eax", "ebx");
        String fourthLine = operationBuilder("mov", addBracketsIfVariable(result), "eax");

        var lines = new ArrayList<String>();
        lines.add(firstLine);
        lines.add(secondLine);
        lines.add(thirdLine);
        lines.add(fourthLine);

		return lines;
	}

    private static ArrayList<String> translateMultiplication(IntermediateCodeInstruction instruction, String operation) {
        String firstArgument = instruction.getArgument1();
        String secondArgument = instruction.getArgument2();
        String result = instruction.getResult();

        String firstLine = operationBuilder("mov", "eax", addBracketsIfVariable(firstArgument));
        String secondLine = operationBuilder("mov", "ebx", addBracketsIfVariable(secondArgument));
        String thirdLine = operationBuilder(operation, "ebx");
        String fourthLine = operationBuilder("mov", addBracketsIfVariable(result), "eax");

        var lines = new ArrayList<String>();
        lines.add(firstLine);
        lines.add(secondLine);
        lines.add(thirdLine);
        lines.add(fourthLine);

		return lines;
	}

    private static String operationBuilder(String operator, String argument) {
		return operator + " " + argument;
	}

	private static String operationBuilder(String operator, String argument1, String argument2) {
        return operator + " " + argument1 + ", " + argument2;
    }

    private static String addBracketsIfVariable(String argument) {
        if (isInteger(argument)) {
            return argument;
        } else {
            return "[" + argument + "]";
        }
    }
}
