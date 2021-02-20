package com.jpl.fyp;

import java.nio.file.Files;
import java.nio.file.Path;

import com.jpl.fyp.classLibrary.DiagnosticSettings;
import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.nodes.RootNode;
import com.jpl.fyp.compilerComponent.IntermediateCodeGenerator;
import com.jpl.fyp.compilerComponent.Lexer;
import com.jpl.fyp.compilerComponent.Parser;
import com.jpl.fyp.compilerComponent.Validator;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            // TODO: should print help message, showing flags
            System.out.println("print help");
        }
        else {
            String sourceCode = Files.readString(Path.of(args[0]));
            DiagnosticSettings diagnosticSettings = Main.getDiagnosticSettings(args);
            Main.start(sourceCode, diagnosticSettings);
        }
    }

	private static DiagnosticSettings getDiagnosticSettings(String[] args) {
        var argument = "";
        if (args.length > 1) {
            argument = args[1];
        }
        else {
            argument = "";
        }
        return new DiagnosticSettings(argument);
	}

	private static void start(String sourceCode, DiagnosticSettings diagnosticSettings) throws Exception {
        Token[] tokens = Lexer.convertSourceCodeToTokens(sourceCode);
        RootNode syntaxTree = Parser.parse(tokens);
        Validator.validate(syntaxTree);
        IntermediateCodeInstruction[] intermediateCode = IntermediateCodeGenerator.generateIntermediateCode(syntaxTree);
        maybePrintDiagnosticInformation(diagnosticSettings, tokens, syntaxTree, intermediateCode);
	}

	private static void maybePrintDiagnosticInformation(DiagnosticSettings diagnosticSettings, Token[] tokens, RootNode syntaxTree, IntermediateCodeInstruction[] intermediateCode) {
        if (diagnosticSettings.isPrintLexerOutput()) {
            printTokenList(tokens);
        }
        if (diagnosticSettings.isPrintParserOutput()) {
            System.out.println(syntaxTree);
        }
        if (diagnosticSettings.isPrintIntermediateRepresentationGeneratorOutput()) {
            printInstructionList(intermediateCode);
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
