package com.jpl.fyp.compilerComponent;

import java.util.ArrayList;
import java.util.Collection;

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
                lines.addAll(translateAdd(instruction));
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

        String firstLine;
        if (isInteger(instructionArgument)) {
            firstLine = "mov eax, " + instructionArgument;
        } else {
            firstLine = "mov eax, [" + instructionArgument + "]";
        }
        String secondLine = "mov [" + instructionResult + "], eax";

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

	private static ArrayList<String> translateAdd(IntermediateCodeInstruction instruction) {
        var lines = new ArrayList<String>();

        var firstLine = "mov eax, " + instruction.getArgument1();
        var secondLine = "mov ebx, " + instruction.getArgument2();
        var thirdLine = "add eax, ebx";
        var fourthLine = "mov [" + instruction.getResult() + "], eax";

        lines.add(firstLine);
        lines.add(secondLine);
        lines.add(thirdLine);
        lines.add(fourthLine);
        
		return lines;
	}

}
