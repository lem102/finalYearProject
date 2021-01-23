package com.jpl.fyp.classLibrary;

public class DiagnosticSettings {
    
    private boolean printLexerOutput;
    private boolean printParserOutput;
    private boolean printIntermediateRepresentationGeneratorOutput;
    
    public DiagnosticSettings(String argument) {
        this.printLexerOutput = argument.contains("l");
        this.printParserOutput = argument.contains("p");
        this.printIntermediateRepresentationGeneratorOutput = argument.contains("i");
	}

	public boolean isPrintLexerOutput() {
		return printLexerOutput;
	}

	public boolean isPrintParserOutput() {
		return printParserOutput;
	}

	public boolean isPrintIntermediateRepresentationGeneratorOutput() {
		return printIntermediateRepresentationGeneratorOutput;
	}
}
