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
            case Declare: 
            case Assign: {
                lines.addAll(translateDeclareOrAssign(instruction));
                break;
            }
            case IfFalseGoto: {
                lines.addAll(translateIfFalseGoto(instruction));
                break;
            }
            case Goto: {
                lines.add(translateGoto(instruction));
                break;
            }
            case LabelCall: {
                lines.addAll(translateLabelCall(instruction));
                break;
            }
            case PushParameter: {
                lines.add(translatePushParameter(instruction));
                break;
            }
            case PopParameter: {
                lines.add(translatePopParameter(instruction));
                break;
            }
            case Print: {
                lines.addAll(translatePrint(instruction));
                break;
            }
            case Return: {
                lines.addAll(translateReturn(instruction));
                break;
            }
            case Add: {
                lines.addAll(translateArithmeticOperation(instruction, "add"));
                break;
            }
            case Subtract: {
                lines.addAll(translateArithmeticOperation(instruction, "sub"));
                break;
            }
            case Multiply: {
                lines.addAll(translateArithmeticOperation(instruction, "mul"));
                break;
            }
            case Divide: {
                lines.addAll(translateArithmeticOperation(instruction, "div"));
                break;
            }
            case Equal: {
                lines.addAll(translateValueComparison(instruction, "sete"));
                break;
            }
            case NotEqual: {
                lines.addAll(translateValueComparison(instruction, "setne"));
                break;
            }
            case LessThan: {
                lines.addAll(translateValueComparison(instruction, "setl"));
                break;
            }
            case LessThanOrEqualTo: {
                lines.addAll(translateValueComparison(instruction, "setle"));
                break;
            }
            case GreaterThan: {
                lines.addAll(translateValueComparison(instruction, "setg"));
                break;
            }
            case GreaterThanOrEqualTo: {
                lines.addAll(translateValueComparison(instruction, "setge"));
                break;
            }
            case And: {
                lines.addAll(translateLogicalComparison(instruction, "and"));
                break;
            }
            case Or: {
                lines.addAll(translateLogicalComparison(instruction, "or"));
                break;
            }
            // case BeginFunction: {
            //     lines.addAll(translateBeginFunction(instruction));
            //     break;
            // }
            case EndFunction: {
                lines.addAll(translateEndFunction(instruction));
            }
            default: {
                lines.add("; instruction untranslated");
                break;
            }
        }
        return lines;
	}

	private static String translatePopParameter(IntermediateCodeInstruction instruction) {
        String argumentName = instruction.getArgument1();
        String register = convertToRegister(instruction.getResult());
		return operationBuilder("mov", addBracketsIfVariable(argumentName), register);
	}

	private static String translatePushParameter(IntermediateCodeInstruction instruction) {
        String argument = instruction.getArgument1();
        String register = convertToRegister(instruction.getResult());
        return operationBuilder("mov", register, addBracketsIfVariable(argument));
	}

	private static String convertToRegister(String registerNumber) {
        switch (registerNumber) {
            case "0": {
                return "eax";
            }
            case "1": {
                return "ebx";
            }
            case "2": {
                return "ecx";
            }
            case "3": {
                return "edx";
            }
            default: {
                System.out.println("convert to register error, not enough registers.");
            }
        }
		return null;
	}

	private static ArrayList<String> translateReturn(IntermediateCodeInstruction instruction) {
        var lines = new ArrayList<String>();
        lines.add(operationBuilder("mov", "eax", addBracketsIfVariable(instruction.getResult())));
        lines.add("ret");
		return lines;
	}

	private static ArrayList<String> translateEndFunction(IntermediateCodeInstruction instruction) {
        var lines = new ArrayList<String>();
        lines.add("ret");
		return lines;
	}

	private static ArrayList<String> translateLabelCall(IntermediateCodeInstruction instruction) {
        String functionToBeCalled = instruction.getArgument1();
        String variableToStoreResultIn = instruction.getResult();

        String callFunction = operationBuilder("call", functionToBeCalled);
        String storeResultInVariable = operationBuilder("mov", addBracketsIfVariable(variableToStoreResultIn), "eax");
        
        var lines = new ArrayList<String>();
        lines.add(callFunction);
        lines.add(storeResultInVariable);
        return lines;
	}

	private static ArrayList<String> translateBeginFunction(IntermediateCodeInstruction instruction) {
        String popEax = operationBuilder("push", "eax");
		return null;
	}

	private static String translateGoto(IntermediateCodeInstruction instruction) {
		return operationBuilder("jmp", instruction.getResult());
	}

	private static ArrayList<String> translateIfFalseGoto(IntermediateCodeInstruction instruction) {
        String expressionResult = instruction.getArgument1();
        String postLoopLabel = instruction.getResult();

        String loadExpressionResultIntoEax = operationBuilder("mov", "eax", addBracketsIfVariable(expressionResult));
        String compare = operationBuilder("cmp", "eax", "1");
        String jumpToPostLoopLabelIfEqual = operationBuilder("jne", postLoopLabel);
        
        var lines = new ArrayList<String>();
        lines.add(loadExpressionResultIntoEax);
        lines.add(compare);
        lines.add(jumpToPostLoopLabelIfEqual);
		return lines;
	}

	private static ArrayList<String> translateLogicalComparison(IntermediateCodeInstruction instruction, String logicalComparison) {
        String firstArgument = instruction.getArgument1();
        String secondArgument = instruction.getArgument2();
        String variableForResultToBeStoredIn = instruction.getResult();

        String loadFirstArgumentIntoEax = operationBuilder("mov", "eax", addBracketsIfVariable(firstArgument));
        String compareSecondArgumentWithEax = operationBuilder(logicalComparison, "eax", addBracketsIfVariable(secondArgument));
        String storeResult = operationBuilder("mov", addBracketsIfVariable(variableForResultToBeStoredIn), "eax");

        var lines = new ArrayList<String>();
        lines.add(loadFirstArgumentIntoEax);
        lines.add(compareSecondArgumentWithEax);
        lines.add(storeResult);
		return lines;
	}

	private static ArrayList<String> translateValueComparison(IntermediateCodeInstruction instruction, String comparison) {
        String firstArgument = instruction.getArgument1();
        String secondArgument = instruction.getArgument2();
        String variableForResultToBeStoredIn = instruction.getResult();

        String loadFirstArgumentIntoEax = operationBuilder("mov", "eax", addBracketsIfVariable(firstArgument));
        String compareSecondArgumentWithEax = operationBuilder("cmp", "eax", addBracketsIfVariable(secondArgument));
        String performComparison = operationBuilder(comparison, "al");
        String convertResult = operationBuilder("movzx", "eax", "al");
        String storeResult = operationBuilder("mov", addBracketsIfVariable(variableForResultToBeStoredIn), "eax");

        var lines = new ArrayList<String>();
        lines.add(loadFirstArgumentIntoEax);
        lines.add(compareSecondArgumentWithEax);
        lines.add(performComparison);
        lines.add(convertResult);
        lines.add(storeResult);
		return lines;
	}

	private static ArrayList<String> translatePrint(IntermediateCodeInstruction instruction) {
        String loadValueToBePrintedIntoEax = operationBuilder("mov", "eax", addBracketsIfVariable(instruction.getResult()));
        String callPrint = operationBuilder("call", "iprintLF");
        
        var lines = new ArrayList<String>();
        lines.add(loadValueToBePrintedIntoEax);
        lines.add(callPrint);
        return lines;
	}

	private static ArrayList<String> translateDeclareOrAssign(IntermediateCodeInstruction instruction) {
        String targetVariableName = instruction.getResult();
        String valueToBeAssigned = instruction.getArgument1();
        if (valueToBeAssigned == null) {
            valueToBeAssigned = "0";
        }
        String loadIntitalValueIntoEax = operationBuilder("mov", "eax", addBracketsIfVariable(valueToBeAssigned));
        String moveInitialValueIntoVariable = operationBuilder("mov", addBracketsIfVariable(targetVariableName), "eax");        
        
		var lines = new ArrayList<String>();
        lines.add(loadIntitalValueIntoEax);
        lines.add(moveInitialValueIntoVariable);
        return lines;
	}


    private static ArrayList<String> translateArithmeticOperation(IntermediateCodeInstruction instruction, String operation) {
        String firstArgument = instruction.getArgument1();
        String secondArgument = instruction.getArgument2();
        String variableForResultToBeStoredIn = instruction.getResult();

        String loadFirstValueIntoEax = operationBuilder("mov", "eax", addBracketsIfVariable(firstArgument));
        String loadSecondValueIntoEbx = operationBuilder("mov", "ebx", addBracketsIfVariable(secondArgument));
        String performOperation = createPerformOperationAssembly(operation);
        String moveResultIntoVariable = operationBuilder("mov", addBracketsIfVariable(variableForResultToBeStoredIn), "eax");

        var lines = new ArrayList<String>();
        lines.add(loadFirstValueIntoEax);
        lines.add(loadSecondValueIntoEbx);
        lines.add(performOperation);
        lines.add(moveResultIntoVariable);
		return lines;
    }

	private static String createPerformOperationAssembly(String operation) {
        switch (operation) {
            case "mul":
            case "div": {
                return operationBuilder(operation, "ebx");
            }
            case "add":
            case "sub": {
                return operationBuilder(operation, "eax", "ebx");
            }
        }
        return null;
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

	private static boolean isInteger(String string) {
		try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
	}
}
