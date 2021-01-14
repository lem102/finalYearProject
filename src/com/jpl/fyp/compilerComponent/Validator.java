package com.jpl.fyp.compilerComponent;

import com.jpl.fyp.classLibrary.JPLException;
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
}
