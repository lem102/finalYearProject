package com.jpl.fyp.compilerComponent;

import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.nodes.*;

public class Validator {
    public static void validate(RootNode syntaxTree) throws JPLException {
        // in this method i will run various validation methods
        // i need some ideas of what i can validate

        // check that identifiers absent from the syntax table are not used
        checkSyntaxTreeForUndeclaredIdentifiers(syntaxTree);

        // validate use of identifiers that arent declared
        // checkForVariableUseBeforeDeclaration(syntaxTree);

        // validate use of identifiers that arent declared in expressions

        syntaxTree.beginValidation();
    }

	private static void checkSyntaxTreeForUndeclaredIdentifiers(RootNode syntaxTree) {
        for (StatementNode node : syntaxTree.getStatements()) {
            checkNodeForUndeclaredIdentifiers(node);
        }
	}

	private static void checkNodeForUndeclaredIdentifiers(StatementNode node) {
        // have a method on each node type for post tree construction validation, this method can call child statements in the case of containing nodes and in the sub classes of statement node add type specific post tree construction validation.
        
	}

	public static void validateIdentifierIsDeclared(SymbolTableEntry[] entries, String identifier) throws JPLException {
        if (!isIdentifierDeclared(entries, identifier)) {
            throw new JPLException("Assignment Node (Validation) : undeclared identifier (" + identifier + ") used.");
        }
	}

	private static boolean isIdentifierDeclared(SymbolTableEntry[] entries, String identifier) {
		var symbolPresent = false;
        for (SymbolTableEntry entry : entries) {
            if (identifier.equals(entry.getName())) {
                symbolPresent = true;
            }
        }
		return symbolPresent;
	}

	public static void validateNumberOfArguments(SymbolTableEntry[] entries, String identifier, ExpressionNode[] parametersOfFunctionCall) throws JPLException {
        SymbolTableEntry symbol = findSymbolTableEntry(entries, identifier);
        if (symbol.getArguments().length != parametersOfFunctionCall.length) {
            throw new JPLException("Validation: incorrect number of arguments.");
        }
	}

	private static SymbolTableEntry findSymbolTableEntry(SymbolTableEntry[] entries, String identifier) throws JPLException {
		for (SymbolTableEntry entry : entries) {
            if (identifier.equals(entry.getName())) {
                return entry;
            }
        }
        throw new JPLException("Validation: symbol not found in symbol table.");
	}
}
