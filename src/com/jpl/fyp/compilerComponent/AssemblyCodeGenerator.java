package com.jpl.fyp.compilerComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.nodes.RootNode;

public class AssemblyCodeGenerator {
    public static String[] generateAssembly(IntermediateCodeInstruction[] intermediateCode, RootNode syntaxTree) {
        ArrayList<String> dataSection = generateDataSection();
        ArrayList<String> bssSection = generateBssSection(syntaxTree);
        ArrayList<String> textSection = generateTextSection(intermediateCode);

        var assembly = new ArrayList<String>();
        assembly.add("%include \"functions.asm\"");
        assembly.addAll(dataSection);
        assembly.addAll(bssSection);
        assembly.addAll(textSection);
        return assembly.toArray(new String[0]);
    }

    private static ArrayList<String> generateDataSection() {
        var lines = new ArrayList<String>();
        lines.add(createSectionHeader("data"));
        return lines;
    }

    private static ArrayList<String> generateBssSection(RootNode syntaxTree) {
        var lines = new ArrayList<String>();
        lines.add(createSectionHeader("bss"));
        lines.addAll(createVariableStorage(syntaxTree));
        return lines;
    }

    private static ArrayList<String> createVariableStorage(RootNode syntaxTree) {
        var lines = new ArrayList<String>();
        SymbolTableEntry[] declaredSymbols = syntaxTree.getAllVariableSymbols();
        lines.addAll(createDeclaredVariableStorage(declaredSymbols));
        int numberOfTemporaryVariables = IntermediateCodeInstruction.getCurrentTemporaryVariableIndex();
        lines.addAll(createTemporaryVariableStorage(numberOfTemporaryVariables));
        String[] parameterNames = syntaxTree.getAllParameterNames();
        lines.addAll(createParameterVariableStorage(parameterNames));
        return lines;
    }

	private static ArrayList<String> createParameterVariableStorage(String[] parameterNames) {
        var lines = new ArrayList<String>();
        for (String name : parameterNames) {
            lines.add(name + ": resb 4");
        }
		return lines;
	}

	private static ArrayList<String> createDeclaredVariableStorage(SymbolTableEntry[] declaredSymbols) {
        var lines = new ArrayList<String>();
        for (SymbolTableEntry symbol : declaredSymbols) {
            lines.add(symbol.getName() + ": resb 4");
        }
		return lines;
	}

	private static ArrayList<String> createTemporaryVariableStorage(int numberOfTemporaryVariables) {
        var lines = new ArrayList<String>();
        for (int i = 0; i < numberOfTemporaryVariables; i++) {
            lines.add("t" + i + ": resb 4");
        }
		return lines;
	}

	private static ArrayList<String> generateTextSection(IntermediateCodeInstruction[] intermediateCode) {
        var lines = new ArrayList<String>();
        lines.add(createSectionHeader("text"));
        lines.addAll(createStartGlobalAndLabel());
        lines.addAll(IntermediateCodeTranslator.translateIntermediateCodeIntoAssembly(intermediateCode));
        return lines;
    }

    private static ArrayList<String> createStartGlobalAndLabel() {
        String[] lines = {
            "global _start",
            "_start:",
            "call main",
            "call exit",
        };
        return new ArrayList<String>(Arrays.asList(lines));
    }

    private static String createSectionHeader(String sectionName) {
        return "section ." + sectionName;
    }
}
