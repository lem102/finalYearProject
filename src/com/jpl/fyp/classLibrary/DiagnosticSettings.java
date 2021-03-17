package com.jpl.fyp.classLibrary;

public class DiagnosticSettings {
    
    private boolean printLexerOutput;
    private boolean printParserOutput;
    private boolean printIntermediateRepresentationGeneratorOutput;
	private boolean printAssembly;
    
    public DiagnosticSettings(String argument) {
        this.printLexerOutput = argument.contains("l");
        this.printParserOutput = argument.contains("p");
        this.printIntermediateRepresentationGeneratorOutput = argument.contains("i");
        this.printAssembly = argument.contains("a");
	}

	public boolean printLexerOutput() {
		return printLexerOutput;
	}

	public boolean printParserOutput() {
		return printParserOutput;
	}

	public boolean printIntermediateRepresentationGeneratorOutput() {
		return printIntermediateRepresentationGeneratorOutput;
	}

	public boolean printAssembly() {
		return this.printAssembly;
	}
}
