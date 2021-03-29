package com.jpl.fyp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.jpl.fyp.classLibrary.DiagnosticSettings;
import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.nodes.RootNode;
import com.jpl.fyp.compilerComponent.AssemblyCodeGenerator;
import com.jpl.fyp.compilerComponent.IntermediateCodeGenerator;
import com.jpl.fyp.compilerComponent.Lexer;
import com.jpl.fyp.compilerComponent.Parser;
import com.jpl.fyp.compilerComponent.Validator;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            // TODO: should print help message, showing flags
            System.out.println("l for lexer, p for parser, i for intermediate representation/3 address code.");
        }
        else {
            String sourceCode = Files.readString(Path.of(args[0]));
            DiagnosticSettings diagnosticSettings = getDiagnosticSettings(args);
            start(sourceCode, diagnosticSettings);
        }
    }

	private static DiagnosticSettings getDiagnosticSettings(String[] args) {
        var argument = "";
        if (args.length > 1) {
            argument = args[1];
        }
        return new DiagnosticSettings(argument);
	}

	private static void start(String sourceCode, DiagnosticSettings diagnosticSettings) throws Exception {
        Token[] tokens = Lexer.convertSourceCodeToTokens(sourceCode);
        RootNode syntaxTree = Parser.parse(tokens);
        Validator.validate(syntaxTree);
        IntermediateCodeInstruction[] intermediateCode = IntermediateCodeGenerator.generateIntermediateCode(syntaxTree);
        String[] assembly = AssemblyCodeGenerator.generateAssembly(intermediateCode, syntaxTree);
        writeAssemblyToFile(assembly);
        maybePrintDiagnosticInformation(diagnosticSettings, tokens, syntaxTree, intermediateCode, assembly);
	}

	private static void writeAssemblyToFile(String[] assembly) {
        Path outputFileName = Path.of("output.asm");
        try {
            Files.writeString(outputFileName, convertArrayToString(assembly));
        } catch (IOException exception) {
            System.out.println(exception);
        }
	}

	private static String convertArrayToString(String[] assembly) {
        String output = "";
        for (String line : assembly) {
            output += line + "\n";
        }
		return output;
	}

	private static void printAssembly(String[] assembly) {
        for (String line : assembly) {
            System.out.println(line);
        }
	}

	private static void maybePrintDiagnosticInformation(DiagnosticSettings diagnosticSettings, Token[] tokens, RootNode syntaxTree, IntermediateCodeInstruction[] intermediateCode, String[] assembly) {
        if (diagnosticSettings.printLexerOutput()) {
            printTokenList(tokens);
        }
        if (diagnosticSettings.printParserOutput()) {
            System.out.println(syntaxTree);
        }
        if (diagnosticSettings.printIntermediateRepresentationGeneratorOutput()) {
            printInstructionList(intermediateCode);
        }
        if (diagnosticSettings.printAssembly()) {
            printAssembly(assembly);
        }
	}

	private static void printTokenList(Token[] tokens) {
        for (Token token : tokens) {
            System.out.println(token);
        }
	}

    private static void printInstructionList(IntermediateCodeInstruction[] instructions) {
        for (IntermediateCodeInstruction instruction : instructions) {
            System.out.println(instruction);
        }
	}
}
