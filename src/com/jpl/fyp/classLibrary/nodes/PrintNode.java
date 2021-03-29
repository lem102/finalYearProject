package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.IntermediateCodeInstructionType;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class PrintNode extends StatementNode {
	private ExpressionNode expressionToPrint;

	public PrintNode(Token[] tokens) throws JPLException {
        this.validateTokens(tokens);
        Token[] expressionTokens = Arrays.copyOfRange(tokens, 1, tokens.length-1);
        this.expressionToPrint = new ExpressionNode(expressionTokens);
	}

	private void validateTokens(Token[] tokens) throws JPLException {
        if (tokens[0].tokenType != TokenType.Print) {
            throw new JPLException("Print Statement : First token must be an print token.");
        } else if (tokens[tokens.length-1].tokenType != TokenType.Semicolon) {
            throw new JPLException("Print Statement : Last token must be a semicolon.");
        }
	}

	@Override
	public String toString() {
        return "Print Statement:\n"
            + "(\n"
            + this.expressionToPrint + "\n"
            + ")\n";
    }

    @Override
    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        ArrayList<IntermediateCodeInstruction> printExpressionInstructions = this.expressionToPrint.generateIntermediateCode();
        String expressionResultVariableName = super.getExpressionResultVariableName(this.expressionToPrint.getRootExpressionElementNode().getToken(), printExpressionInstructions);

        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.addAll(printExpressionInstructions);
        instructions.add(this.generatePrintInstruction(expressionResultVariableName));
        return instructions;
    }

	private IntermediateCodeInstruction generatePrintInstruction(String thing) {
		return new IntermediateCodeInstruction(IntermediateCodeInstructionType.Print,
                                               null,
                                               null,
                                               thing);
	}
}
