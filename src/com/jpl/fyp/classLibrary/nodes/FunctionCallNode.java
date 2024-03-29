package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jpl.fyp.classLibrary.ExpressionInstructionInformation;
import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.IntermediateCodeInstructionType;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;
import com.jpl.fyp.compilerComponent.Validator;

public class FunctionCallNode extends StatementNode {
    public String identifier;

    public ExpressionNode[] arguments;

    public FunctionCallNode(Token[] tokens) throws JPLException {
        this.validateTokens(tokens);
        this.identifier = tokens[0].tokenValue;
        Token[] argumentTokens = Arrays.copyOfRange(tokens, 2, tokens.length - 2);
        this.arguments = parseArgumentTokens(argumentTokens);
    }

    private ExpressionNode[] parseArgumentTokens(Token[] tokens) throws JPLException {
        if (tokens.length == 0) {
            return new ExpressionNode[0];
        }
        
        var output = new ArrayList<ExpressionNode>();

        for (Token[] tokenArray : splitTokensByArgument(tokens)) {
            output.add(new ExpressionNode(tokenArray));
        }

        return output.toArray(new ExpressionNode[output.size()]);
	}

	private Token[][] splitTokensByArgument(Token[] tokens) {
        int argumentStartIndex = 0;
        List<Token[]> output = new ArrayList<Token[]>();

        for (int tokenIndex = 0; tokenIndex < tokens.length; tokenIndex++) {
            if (tokens[tokenIndex].tokenType == TokenType.Comma) {
                output.add(Arrays.copyOfRange(tokens, argumentStartIndex, tokenIndex));
                argumentStartIndex = tokenIndex + 1;
            }
        }
        output.add(Arrays.copyOfRange(tokens, argumentStartIndex, tokens.length));
        return output.toArray(new Token[output.size()][]);
	}

	private void validateTokens(Token[] tokens) throws JPLException {
        if (tokens[0].tokenType != TokenType.Identifier) {
            throw new JPLException("Function Call Node : First token must be an identifier.");
        }
        else if (tokens[1].tokenType != TokenType.OpeningParenthesis) {
            throw new JPLException("Function Call Node : Second token must be a opening parenthesis.");
        }
        else if (tokens[tokens.length - 2].tokenType != TokenType.ClosingParenthesis) {
            throw new JPLException("Function Call Node : Second to last token must be a closing parenthesis.");
        }
        else if (tokens[tokens.length - 1].tokenType != TokenType.Semicolon) {
            throw new JPLException("Function Call Node : Last token must be a semicolon.");
        }
	}

	@Override
    public String toString()
    {
        String output = "";
        output += "Function Call:\n";
        output += "Identifier: " + identifier + "\n";

        for (ExpressionNode argument : arguments)
        {
            output += argument + "\n";
        }

        return output;
    }

    @Override
	public void validate(SymbolTableEntry[] entries) throws JPLException {
        Validator.validateIdentifierIsDeclared(entries, this.identifier);
        Validator.validateNumberOfArguments(entries, this.identifier, this.arguments);
        for (ExpressionNode argument : this.arguments) {
            argument.validate(entries);
        }
	}

    @Override
    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        var argumentInstructionInformation = new ExpressionInstructionInformation(this.arguments);
        ArrayList<IntermediateCodeInstruction> instructionsForArguments = argumentInstructionInformation.getExpressionInstructions();
        ArrayList<String> expressionResultVariableNames = argumentInstructionInformation.getExpressionResultVariableNames();
        
        ArrayList<IntermediateCodeInstruction> pushParameterInstructions = generatePushParameterInstructions(expressionResultVariableNames);
        IntermediateCodeInstruction labelCallInstruction = this.generateLabelCallInstruction(this.identifier);
        
        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.addAll(instructionsForArguments);
        instructions.addAll(pushParameterInstructions);
        instructions.add(labelCallInstruction);
        
        return instructions;
    }

	private ArrayList<IntermediateCodeInstruction> generatePushParameterInstructions(ArrayList<String> expressionResultVariableNames) {
		var pushParamaterInstructions = new ArrayList<IntermediateCodeInstruction>();
        for (int i = 0; i < expressionResultVariableNames.size(); i++) {
            pushParamaterInstructions.add(this.generatePushParameterInstruction(expressionResultVariableNames.get(i), i));
        }
		return pushParamaterInstructions;
	}

	private IntermediateCodeInstruction generatePushParameterInstruction(String resultVariableName, int registerIndex) {
		return new IntermediateCodeInstruction(IntermediateCodeInstructionType.PushParameter,
                                               resultVariableName,
                                               null,
                                               Integer.toString(registerIndex));
	}

	private IntermediateCodeInstruction generateLabelCallInstruction(String functionName) {
		return new IntermediateCodeInstruction(IntermediateCodeInstructionType.LabelCall,
                                               functionName,
                                               null,
                                               IntermediateCodeInstruction.getNewTemporaryVariableName());
	}
}
