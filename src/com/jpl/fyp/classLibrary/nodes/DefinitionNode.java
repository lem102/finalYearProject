package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.IntermediateCodeInstructionType;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.JPLType;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class DefinitionNode extends ContainingNode {
    public String definitionName;
    public ArgumentNode[] arguments;

    public DefinitionNode(Token[] tokens) throws JPLException {
        this.validateTokens(tokens);
        this.definitionName = tokens[1].tokenValue;
        this.arguments = parseArguments(Arrays.copyOfRange(tokens, 3, tokens.length - 2));
        setSymbolTableEntry(new SymbolTableEntry(JPLType.Integer, this.definitionName, this.arguments));
    }

    private ArgumentNode[] parseArguments(Token[] tokens) throws JPLException {
        var arguments = new ArrayList<ArgumentNode>();

        for (int i = 0; i < tokens.length; i+=3) {
            var argument = new ArgumentNode();
            switch (tokens[i].tokenType) {
                case IntegerDeclaration: {
                    argument.setType(JPLType.Integer);
                    break;
                } default: {
                    throw new JPLException("Definition Node : Unkown type.");
                }
            }
            argument.getIdentifier(tokens[i+1].tokenValue);
            arguments.add(argument);
        }
		return arguments.toArray(new ArgumentNode[arguments.size()]);
	}

	private void validateTokens(Token[] tokens) throws JPLException {
        if (tokens[0].tokenType != TokenType.Define) {
            throw new JPLException("Definition Node : First token must be a definition token.");
        }
        if (tokens[1].tokenType != TokenType.Identifier) {
            throw new JPLException("Definition Node : Second token must be a identifier token.");
        }
        if (tokens[2].tokenType != TokenType.OpeningParenthesis) {
            throw new JPLException("Definition Node : Third Token must be an opening parenthese.");
        }
        if (tokens[tokens.length - 2].tokenType != TokenType.ClosingParenthesis) {
            throw new JPLException("Definition Node : Second to last token must be a closing parenthesis.");
        }
        if (tokens[tokens.length - 1].tokenType != TokenType.OpeningBrace) {
            throw new JPLException("Definition Node : Last token must be an opening brace.");
        }
        validateArgumentTokens(Arrays.copyOfRange(tokens, 3, tokens.length - 2));
	}

	private void validateArgumentTokens(Token[] tokens) throws JPLException {
        for (int i = 0; i < tokens.length; i++) {
            switch (i % 3) {
                case 0: {
                    if (tokens[i].tokenType != TokenType.IntegerDeclaration) {
                        throw new JPLException("Definition Node : arguments must have a type.");
                    }
                    break;
                }
                case 1: {
                    if (tokens[i].tokenType != TokenType.Identifier) {
                        throw new JPLException("Definition Node : arguments must have an identifier.");
                    }
                    break;
                }
                case 2: {
                    if (tokens[i].tokenType != TokenType.Comma) {
                        throw new JPLException("Definition Node : arguments must be separated by a comma.");
                    }
                    break;
                }
            }
        }
	}

	@Override
    public String toString() {
        return "function: " + definitionName + "\n"
            + "(\n"
            + this.argumentsToString()
            + ")\n"
            + super.toString();
    }

	private String argumentsToString() {
		String stringOfArguments = "";
        for (ArgumentNode argument : this.arguments) {
            stringOfArguments += argument;
        }
        return stringOfArguments;
	}

    @Override
    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.add(super.generateLabelInstruction(this.definitionName));
        instructions.addAll(this.generatePopParameterInstructions(this.arguments));
        instructions.add(this.generateBeginFunctionInstruction());
        instructions.addAll(super.generateStatementInstructions());
        instructions.add(this.generateEndFunctionInstruction());
        return instructions;
    }

	private ArrayList<IntermediateCodeInstruction> generatePopParameterInstructions(ArgumentNode[] arguments) {
        var instructions = new ArrayList<IntermediateCodeInstruction>();
        for (int argumentIndex = 0; argumentIndex < arguments.length; argumentIndex++) {
            instructions.add(this.generatePopParameterInstruction(arguments[argumentIndex].getName(), argumentIndex));
        }
		return instructions;
	}

	private IntermediateCodeInstruction generatePopParameterInstruction(String argumentName, int argumentIndex) {
        return new IntermediateCodeInstruction(IntermediateCodeInstructionType.PopParameter,
                                               argumentName,
                                               null,
                                               Integer.toString(argumentIndex));
	}

	private IntermediateCodeInstruction generateEndFunctionInstruction() {
		return new IntermediateCodeInstruction(IntermediateCodeInstructionType.EndFunction,
                                               null,
                                               null,
                                               null);
	}

	private IntermediateCodeInstruction generateBeginFunctionInstruction() {
		return new IntermediateCodeInstruction(IntermediateCodeInstructionType.BeginFunction,
                                               null,
                                               null,
                                               null);
	}

    public ArrayList<String> getArgumentNames() {
        var names = new ArrayList<String>();
        for (ArgumentNode argument : this.arguments) {
            names.add(argument.getName());
        }
        return names;
	}
}
