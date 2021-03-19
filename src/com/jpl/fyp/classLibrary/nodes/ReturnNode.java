package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.IntermediateCodeInstructionType;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class ReturnNode extends StatementNode {

	private ExpressionNode expressionToReturn;

	public ReturnNode(Token[] tokens) throws JPLException {
        this.validateTokens(tokens);
        Token[] expressionTokens = Arrays.copyOfRange(tokens, 1, tokens.length-1);
        this.expressionToReturn = new ExpressionNode(expressionTokens);
	}

	private void validateTokens(Token[] tokens) throws JPLException {
        if (tokens[0].tokenType != TokenType.Return) {
            throw new JPLException("Return Statement : First token must be an return token.");
        } else if (tokens[tokens.length-1].tokenType != TokenType.Semicolon) {
            throw new JPLException("Return Statement : Last token must be a semicolon.");
        }
	}

    @Override
    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        ArrayList<IntermediateCodeInstruction> returnExpressionInstructions = this.expressionToReturn.generateIntermediateCode();
        String expressionResultVariableName = super.getExpressionResultVariableName(this.expressionToReturn.getRootExpressionElementNode().getToken(), returnExpressionInstructions);

        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.addAll(returnExpressionInstructions);
        instructions.add(this.generateReturnInstruction(expressionResultVariableName));
        return instructions;
    }

	private IntermediateCodeInstruction generateReturnInstruction(String thing) {
		return new IntermediateCodeInstruction(IntermediateCodeInstructionType.Return,
                                               null,
                                               null,
                                               thing);
	}
}
